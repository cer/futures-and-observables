package net.chrisrichardson.productpage.resttemplate.backend.services.javafutures

import org.springframework.stereotype.Component
import org.springframework.scheduling.annotation.{AsyncResult, Async}

import akka.actor.ActorSystem
import org.springframework.beans.factory.annotation.Autowired
import net.chrisrichardson.productpage.resttemplate.backend.dtos.ProductInfo
import com.netflix.hystrix.{HystrixCommandGroupKey, HystrixCommandKey, HystrixCommand}
import org.springframework.web.client.RestTemplate
import net.chrisrichardson.productpage.resttemplate.backend.util.RestTemplateFactory

trait ProductInfoService {
  def getProductInfo(productId: Long): java.util.concurrent.Future[ProductInfo]

  def getProductInfoUsingHystrix(productId: Long): java.util.concurrent.Future[ProductInfo]
}

@Component("productInfoService_javaFutures")
class ProductInfoServiceImpl extends ProductInfoService {

  @Async
  override def getProductInfo(productId: Long): java.util.concurrent.Future[ProductInfo] = {
    new AsyncResult(ProductInfo(productId, "Toothbrush"))
  }

  val restTemplate = RestTemplateFactory.makeRestTemplate()

  class GetProductInfoCommand(productId: Long)
    extends HystrixCommand[ProductInfo](HystrixCommandGroupKey.Factory.asKey("ProductInfoGroup")) {

    override def run() = {
      val baseUrl = System.getProperty("net.chrisrichardson.resttemplate.baseurl")
      restTemplate.getForEntity("{baseUrl}/productinfo/{productId}", classOf[ProductInfo], baseUrl, productId: java.lang.Long).getBody
    }
  }

  def getProductInfoUsingHystrix(productId: Long) = {
    new GetProductInfoCommand(productId).queue()
  }

}


