package net.chrisrichardson.productpage.resttemplate.backend.services.scalafutures

import org.springframework.stereotype.Component
import org.springframework.beans.factory.annotation.Autowired
import net.chrisrichardson.productpage.resttemplate.backend.dtos.{Recommendations, ProductDetails}
import scala.concurrent.ExecutionContext.Implicits.global

@Component("productDetailsService_scalaFutures")
class ProductDetailsService @Autowired()(productInfoService: ProductInfoService,
                                         reviewService: ReviewService,
                                         recommendationService: RecommendationService) {


  def getProductDetails(productId: Long) = {
    val productInfoFuture = productInfoService.getProductInfo(productId)
    val recommendationsFuture = recommendationService.getRecommendations(productId).recover {
      case _ => Recommendations(List())
    }
    val reviewsFuture = reviewService.getReviews(productId)

    for (((productInfo, recommendations), reviews) <-
                               productInfoFuture zip recommendationsFuture zip reviewsFuture)
          yield ProductDetails(productInfo, recommendations, reviews)
  }

  def getProductDetailsUsingScalaz(productId: Long) = {

    // Using Scalaz applicative |@| to combine three futures

    import scalaz._
    import Scalaz._
    import ScalazFutures._

    val productDetailsFuture = productInfoService.getProductInfo(productId)
    val recommendationsFuture = recommendationService.getRecommendations(productId).recover {
      case _ => Recommendations(List())
    }
    val reviewsFuture = reviewService.getReviews(productId)


    (productDetailsFuture |@| recommendationsFuture |@| reviewsFuture) {
      ProductDetails(_, _, _)
    }
  }

}
