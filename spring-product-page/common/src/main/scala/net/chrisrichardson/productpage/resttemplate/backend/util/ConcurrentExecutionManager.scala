package net.chrisrichardson.productpage.resttemplate.backend.util

import scala.concurrent.Future
import akka.actor._
import akka.pattern.{ask, pipe}
import scala.collection.mutable.{HashMap, Queue}
import scala.concurrent.ExecutionContext.Implicits.global
import akka.actor.ActorDSL._
import akka.actor.SupervisorStrategy.Stop
import scala.concurrent.duration._
import java.util.concurrent.RejectedExecutionException
import akka.util.Timeout
import scala.reflect.ClassTag

import org.slf4j.LoggerFactory

class ConcurrentExecutionManager(limit: Int, maxQueueSize: Int) extends Actor {

  val logger = LoggerFactory.getLogger(classOf[ConcurrentExecutionManager])
  
  import ConcurrentExecutionManager._

  case class ExecutableRequest(coordinator: ActorRef, sender: ActorRef, request: Request) {
    def execute() = {
      val f = request.body()
      f pipeTo sender
      f
    }

    def fail(t : Throwable) {
      sender ! Status.Failure(t)
    }
  }

  val requestQueue = Queue[ExecutableRequest]()
  val outstandingChildren = HashMap[ActorRef, ExecutableRequest]()

  def outstanding = outstandingChildren.size

  override val supervisorStrategy =
    OneForOneStrategy() {
      case t: Throwable =>
        logger.debug("child failed!")
        outstandingChildren(sender).fail(t)
        outstandingChildren.remove(sender)
        processNextInQueue()
        Stop
    }

  val notBusy: Receive = {

    case Terminated(child) =>
      logger.debug("removing child")
      outstandingChildren.remove(child)

    case r: Request =>
      executeSafely(ExecutableRequest(self, sender, r))
      checkIfBusy()
  }

  val busy: Receive = {
    case Terminated(child) =>
      logger.debug("removing child")
      outstandingChildren.remove(child)
      processNextInQueue()

    case r: Request if requestQueue.length == maxQueueSize =>
      sender ! akka.actor.Status.Failure(new RejectedExecutionException("max queue size reached"))

    case r: Request =>
      logger.debug("Queuing")
      requestQueue += ExecutableRequest(self, sender, r)

  }


  def processNextInQueue() {
    logger.debug("processNextInQueue")
    if (requestQueue.nonEmpty) {
      logger.debug("executing from queue")
      executeSafely(requestQueue.dequeue())
    } else {
      context.unbecome()
      logger.debug("Not busy")
    }
  }

  def executeSafely(request: ExecutableRequest) = {
    val a = actor(new Act {
      become {
        case r: ExecutableRequest =>
          val s = self
          r.execute() onComplete {
            case _ => self ! PoisonPill
          }

      }
    }
    )

    context.watch(a)
    outstandingChildren(a) = request

    a ! request
  }

  def receive = notBusy

  def checkIfBusy() {
    if (outstanding == limit) {
      logger.debug("Becoming busy: " + (outstanding, limit))
      context.become(busy)
    }
  }
}


object ConcurrentExecutionManager {
  case class Request(body: () => Future[Any])
}

class ConcurrencyLimiter(maxConcurrentRequests: Int, maxQueueSize: Int)(implicit actorSystem : ActorSystem) {

  implicit val timeout = Timeout(5 seconds)

  val concurrentExecutionManager = actorSystem.actorOf(Props(classOf[ConcurrentExecutionManager], maxConcurrentRequests, maxQueueSize))

  def withLimit[T](body : => Future[T])(implicit m: ClassTag[T]) = (concurrentExecutionManager ? ConcurrentExecutionManager.Request { () => body }).mapTo[T]


}