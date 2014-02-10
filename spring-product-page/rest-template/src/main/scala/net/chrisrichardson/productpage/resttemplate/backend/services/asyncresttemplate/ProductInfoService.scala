package net.chrisrichardson.productpage.resttemplate.backend.services.asyncresttemplate

import org.springframework.stereotype.Component
import scala.concurrent.ExecutionContext.Implicits.global

import akka.actor.ActorSystem
import org.springframework.beans.factory.annotation.Autowired

import net.chrisrichardson.productpage.resttemplate.backend.util.RestTemplateFactory._
import net.chrisrichardson.productpage.resttemplate.backend.util.ListenableFutureUtil._
import net.chrisrichardson.productpage.resttemplate.backend.dtos.ProductInfo
import org.springframework.http.ResponseEntity
import scala.concurrent.Future
import org.springframework.util.concurrent.ListenableFuture
import org.springframework.web.servlet.HandlerExceptionResolver
import org.springframework.util.Assert
import org.slf4j.LoggerFactory

trait ProductInfoService {
  def getProductInfo(productId: Long): Future[ProductInfo]

}
@Component("productInfoService_asyncRestTemplate")
class ProductInfoServiceImpl extends ProductInfoService {

  val logger = LoggerFactory.getLogger(classOf[ProductInfoServiceImpl])

  val asyncRestTemplate = makeAsyncRestTemplate()

  override def getProductInfo(productId: Long) = {

    val baseUrl = System.getProperty("net.chrisrichardson.resttemplate.baseurl")
    logger.debug("BaseUrl= " + baseUrl)
    Assert.notNull(baseUrl)
    val listenableFuture: ListenableFuture[ResponseEntity[ProductInfo]] = asyncRestTemplate.getForEntity(baseUrl + "/productinfo/{productId}", classOf[ProductInfo], productId : java.lang.Long)

    toScalaFuture(listenableFuture).map { _.getBody  }

  }



}


