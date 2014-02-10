package net.chrisrichardson.productpage.resttemplate.backend.util

import org.junit._
import akka.actor.{Props, ActorSystem}
import ConcurrentExecutionManager.Request
import akka.pattern.ask
import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import akka.util.Timeout
import java.util.concurrent.{RejectedExecutionException, TimeUnit}

class ConcurrencyLimiterTest {

  implicit val actorSystem = ActorSystem()
  implicit val timeout = Timeout(5 seconds)

  class MyException extends Exception


  @Test
  def something {
    val limiter = new ConcurrencyLimiter(2, 2)
    val result = limiter.withLimit { Future.successful(1)    }
    Assert.assertEquals(1, Await.result(result, 1.second))
  }


  @Test
  def exception {

    val limiter = new ConcurrencyLimiter(2, 2)
    val result = limiter.withLimit { throw new MyException }
    try {
      Await.result(result, 1.second)
      throw new RuntimeException("Expected failure")
    } catch {
      case t: MyException =>
    }
  }

  @Test
  def queue {
    val throttler = actorSystem.actorOf(Props(classOf[ConcurrentExecutionManager], 1, 2))
    val futures = for (i <- 1 to 3) yield {
      (throttler ? Request { () => TimeUnit.MILLISECONDS.sleep(200) ; Future.successful(i) }).mapTo[Int]
    }
    val result = Future.sequence(futures)
    Assert.assertEquals(Vector(1,2,3), Await.result(result, 2.second))
  }

  @Test
  def queueExceeded {
    val throttler = actorSystem.actorOf(Props(classOf[ConcurrentExecutionManager], 1, 2))
    val futures = for (i <- 1 to 4) yield {
      (throttler ? Request { () => TimeUnit.MILLISECONDS.sleep(200) ; Future.successful(i) }).mapTo[Int]
    }
    val result = Future.sequence(futures.take(3))
    Assert.assertEquals(Vector(1,2,3), Await.result(result, 2.second))

    try {
      Await.result(futures(3), 1.second)
      throw new RuntimeException("Expected failure")
    } catch {
      case t: RejectedExecutionException =>
    }

    val result2 = throttler ? Request { () => Future.successful(99)    }
    Assert.assertEquals(99, Await.result(result2, 1.second))

  }

  @Test
  def queueWithFailure {
    val limiter = new ConcurrencyLimiter(1, 3)
    val f0 = limiter.withLimit { TimeUnit.MILLISECONDS.sleep(200) ; throw new MyException }
    val futures = for (i <- 1 to 3) yield {
      limiter.withLimit {TimeUnit.MILLISECONDS.sleep(200) ; Future.successful(i) }.mapTo[Int]
    }
    try {
      Await.result(f0, 1.second)
      throw new RuntimeException("Expected failure")
    } catch {
      case t: MyException =>
    }

    val result = Future.sequence(futures)
    Assert.assertEquals(Vector(1,2,3), Await.result(result, 2.second))
  }

}
