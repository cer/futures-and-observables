package net.chrisrichardson.productpage.resttemplate.backend.services.rx

import org.springframework.stereotype.Component
import org.springframework.beans.factory.annotation.Autowired
import net.chrisrichardson.productpage.resttemplate.backend.dtos.ProductDetails

@Component
class ProductDetailsService @Autowired()(productInfoService: ProductInfoService,
                                         reviewService: ReviewService,
                                         recommendationService: RecommendationService) {

  def getProductDetails(productId: Long) = {
    val productInfo = productInfoService.getProductDetails(productId)
    val recommendations = recommendationService.getRecommendations_rx(productId)
    val reviews = reviewService.getReviews_rx(productId)

    for (((p, r), rv) <- productInfo zip recommendations zip reviews ) yield ProductDetails(p, r, rv)
  }


}
