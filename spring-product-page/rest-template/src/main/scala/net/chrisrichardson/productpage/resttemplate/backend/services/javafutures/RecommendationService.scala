package net.chrisrichardson.productpage.resttemplate.backend.services.javafutures

import org.springframework.stereotype.Component
import org.springframework.scheduling.annotation.{AsyncResult, Async}
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import rx.Observable
import net.chrisrichardson.productpage.resttemplate.backend.dtos.{Recommendations, ProductInfo}


@Component("recommendationService_javaFutures")
class RecommendationService {

  @Async
  def getRecommendations( productId : Long) : java.util.concurrent.Future[Recommendations] = {
    new AsyncResult(Recommendations(List(ProductInfo(productId, "Rifle"))))
  }

}



