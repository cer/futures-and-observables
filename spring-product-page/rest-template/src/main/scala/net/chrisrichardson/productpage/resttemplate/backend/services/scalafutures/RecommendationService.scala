package net.chrisrichardson.productpage.resttemplate.backend.services.scalafutures

import org.springframework.stereotype.Component
import org.springframework.scheduling.annotation.{AsyncResult, Async}
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import rx.Observable
import net.chrisrichardson.productpage.resttemplate.backend.dtos.{Recommendations, ProductInfo}


@Component("recommendationService_scalaFuture")
class RecommendationService {

  def getRecommendations(productId : Long) = {
    productId match {
      case 2 => Future.failed(new UnsupportedOperationException("recommendations unavailable"))
      case _ => Future { Recommendations(List(ProductInfo(productId, "Rifle")))

      }
    }
  }

}



