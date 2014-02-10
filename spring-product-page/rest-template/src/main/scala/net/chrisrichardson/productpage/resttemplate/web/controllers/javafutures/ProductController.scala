package net.chrisrichardson.productpage.resttemplate.web.controllers.javafutures

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.beans.factory.annotation.Autowired
import net.chrisrichardson.productpage.resttemplate.backend.services.javafutures._

@Controller("productController_javaFuture")
@RequestMapping(Array("/javafutures"))
class ProductController @Autowired()(productDetailsService : ProductDetailsService,
                                     productInfoService: ProductInfoService,
                                     reviewService: ReviewService,
                                     recommendationService: RecommendationService) {

  @RequestMapping(Array("/productdetails/{productId}"))
  @ResponseBody
  def productDetails(@PathVariable productId: Long) = productDetailsService.getProductDetails(productId)

  @RequestMapping(Array("/hystrix/productdetails/{productId}"))
  @ResponseBody
  def productDetailsUsingHystrix(@PathVariable productId: Long) = productDetailsService.getProductDetailsUsingHystrix(productId)




}

