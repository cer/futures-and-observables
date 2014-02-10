package net.chrisrichardson.productpage.resttemplate.backend.services.rx

import org.springframework.stereotype.Component
import net.chrisrichardson.productpage.resttemplate.backend.dtos.{Recommendations, ProductInfo}
import rx.lang.scala.Observable


@Component
class RecommendationService {

  def getRecommendations_rx(productId : Long) : Observable[Recommendations] = {
    productId match {
      case 2 => Observable.error(new UnsupportedOperationException("recommendations unavailable"))
      case _ => Observable.items(Recommendations(List(ProductInfo(productId, "Rifle"))))

    }
  }
}



