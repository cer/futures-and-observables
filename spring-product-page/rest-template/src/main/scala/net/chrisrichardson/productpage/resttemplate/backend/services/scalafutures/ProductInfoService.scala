package net.chrisrichardson.productpage.resttemplate.backend.services.scalafutures

import org.springframework.stereotype.Component
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.duration._
import akka.pattern.CircuitBreaker
import akka.actor.ActorSystem
import scala.concurrent.Future
import java.util.concurrent.TimeUnit
import org.springframework.beans.factory.annotation.Autowired
import net.chrisrichardson.productpage.resttemplate.backend.util.ConcurrencyLimiter
import net.chrisrichardson.productpage.resttemplate.backend.util.FutureUtil.TimeoutFuture
import net.chrisrichardson.productpage.resttemplate.backend.dtos.ProductInfo
import org.slf4j.LoggerFactory

trait ProductInfoService {

  def getProductInfo(productId: Long): Future[ProductInfo]

}

@Component("productInfoService_scalaFuture")
class ProductInfoServiceImpl @Autowired()(implicit actorSystem: ActorSystem) extends ProductInfoService {

  val logger = LoggerFactory.getLogger(this.getClass)

  // NOTE: calls that exceed the call timeout still succeed but trip the circuit breaker for subsequent calls.

  val limiter = new ConcurrencyLimiter(maxConcurrentRequests = 2, maxQueueSize = 2)

  val breaker =
    new CircuitBreaker(actorSystem.scheduler,
      maxFailures = 1,
      callTimeout = 100.milliseconds,
      resetTimeout = 1.minute).onOpen(notifyMeOnOpen())

  def notifyMeOnOpen(): Unit =
    logger.debug("My CircuitBreaker is now open, and will not close for one minute")


  override def getProductInfo(productId: Long) = {
    logger.debug("Using circuit breaker")
    implicit val timeout = 300.milliseconds
    val f =
      breaker.withCircuitBreaker {
        limiter.withLimit {
          TimeoutFuture(Future {
            logger.debug("Sleeping 2")
            TimeUnit.MILLISECONDS.sleep(200)
            logger.debug("done sleeping")
            ProductInfo(productId, "Toothbrush")
          })
        }
      }
    logger.debug("Created breaker")
    f
  }


}


