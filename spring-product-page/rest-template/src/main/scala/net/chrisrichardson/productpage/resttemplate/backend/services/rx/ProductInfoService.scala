package net.chrisrichardson.productpage.resttemplate.backend.services.rx

import org.springframework.stereotype.Component

import akka.actor.ActorSystem
import org.springframework.beans.factory.annotation.Autowired
import net.chrisrichardson.productpage.resttemplate.backend.util.RestTemplateFactory._
import org.springframework.http.ResponseEntity
import net.chrisrichardson.productpage.resttemplate.backend.util.ListenableFutureUtil._
import net.chrisrichardson.productpage.resttemplate.backend.dtos.ProductInfo
import rx.lang.scala.Observable

trait ProductInfoService {

  def getProductDetails(productId: Long) : Observable[ProductInfo]


  }

@Component
class ProductInfoServiceImpl  extends ProductInfoService {

   val asyncRestTemplate = makeAsyncRestTemplate()

  override def getProductDetails(productId: Long) = {

    val baseUrl = System.getProperty("net.chrisrichardson.resttemplate.baseurl")

    val responseEntity = asyncRestTemplate.getForEntity(baseUrl +  "/productinfo/{productId}", classOf[ProductInfo], productId : java.lang.Long)

    toRxObservable(responseEntity).map { (r : ResponseEntity[ProductInfo]) => r.getBody }

  }


}


