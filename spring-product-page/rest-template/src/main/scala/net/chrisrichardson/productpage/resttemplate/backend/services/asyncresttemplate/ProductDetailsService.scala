package net.chrisrichardson.productpage.resttemplate.backend.services.asyncresttemplate

import org.springframework.stereotype.Component
import org.springframework.beans.factory.annotation.Autowired
import net.chrisrichardson.productpage.resttemplate.backend.dtos.ProductDetails
import net.chrisrichardson.productpage.resttemplate.backend.services.scalafutures.{ReviewService, RecommendationService}
import scala.concurrent.ExecutionContext.Implicits.global

@Component("productDetailsService_asyncRestTemplate")
class ProductDetailsService @Autowired()(productInfoService: ProductInfoService,
                                         reviewService: ReviewService,
                                         recommendationService: RecommendationService) {


  def getProductDetails(productId: Long) = {
    val productInfoFuture = productInfoService.getProductInfo(productId)
    val recommendationsFuture = recommendationService.getRecommendations(productId)
    val reviewsFuture = reviewService.getReviews(productId)

    for (((productInfo, recommendations), reviews) <-
                               productInfoFuture zip recommendationsFuture zip reviewsFuture)
          yield ProductDetails(productInfo, recommendations, reviews)
  }

}
