package net.chrisrichardson.productpage.resttemplate.web.controllers.scalafutures

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.beans.factory.annotation.Autowired
import net.chrisrichardson.productpage.resttemplate.backend.services.scalafutures._
import org.springframework.web.context.request.async.DeferredResult
import net.chrisrichardson.productpage.resttemplate.backend.dtos.ProductDetails
import net.chrisrichardson.productpage.resttemplate.backend.util.DeferredResultUtil.toDeferredResult

@Controller("productController_scalaFuture")
@RequestMapping(Array("/scalafutures"))
class ProductController @Autowired()(productDetailsService: ProductDetailsService) {


  @RequestMapping(Array("/productdetails/{productId}"))
  @ResponseBody
  def asyncProductInfo(@PathVariable productId: Long): DeferredResult[ProductDetails] = {
    val productDetails = productDetailsService.getProductDetails(productId)

    toDeferredResult(productDetails)
  }


  @RequestMapping(Array("/scalaz/productdetails/{productId}"))
  @ResponseBody
  def asyncProductInfo_Scalaz(@PathVariable productId: Long): DeferredResult[ProductDetails] = {

    val productDetails = productDetailsService.getProductDetailsUsingScalaz(productId)

    toDeferredResult(productDetails)

  }


}

