package net.chrisrichardson.productpage.resttemplate.backend.services.javafutures

import org.springframework.stereotype.Component
import org.springframework.beans.factory.annotation.Autowired
import java.util.concurrent.TimeUnit
import net.chrisrichardson.productpage.resttemplate.backend.dtos.ProductDetails

@Component("ProductDetailsService_JavaFutures")
class ProductDetailsService @Autowired()(productInfoService: ProductInfoService,
                                         reviewService: ReviewService,
                                         recommendationService: RecommendationService) {

  def getProductDetails(productId: Long) = {
    val productInfoFuture = productInfoService.getProductInfo(productId)
    val recommendationsFuture = recommendationService.getRecommendations(productId)
    val reviewsFuture = reviewService.getReviews(productId)

    val productInfo = productInfoFuture.get(300, TimeUnit.MILLISECONDS)
    val recommendations = recommendationsFuture.get(300, TimeUnit.MILLISECONDS)
    val reviews = reviewsFuture.get(300, TimeUnit.MILLISECONDS)

    ProductDetails(productInfo, recommendations, reviews)
  }

  def getProductDetailsUsingHystrix(productId: Long) = {
    val productInfoFuture = productInfoService.getProductInfoUsingHystrix(productId)
    val recommendationsFuture = recommendationService.getRecommendations(productId)
    val reviewsFuture = reviewService.getReviews(productId)

    val productInfo = productInfoFuture.get(300, TimeUnit.MILLISECONDS)
    val recommendations = recommendationsFuture.get(300, TimeUnit.MILLISECONDS)
    val reviews = reviewsFuture.get(300, TimeUnit.MILLISECONDS)

    ProductDetails(productInfo, recommendations, reviews)
  }


}
