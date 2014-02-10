package net.chrisrichardson.productpage.resttemplate.web.controllers.rx

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.context.request.async.DeferredResult
import net.chrisrichardson.productpage.resttemplate.backend.services.rx._
import net.chrisrichardson.productpage.resttemplate.backend.util.RxObservableUtil._
import net.chrisrichardson.productpage.resttemplate.backend.dtos.ProductDetails

@Controller()
@RequestMapping(Array("/rx"))
class ProductController @Autowired()(productDetailsService: ProductDetailsService) {


  @RequestMapping(Array("/productdetails/{productId}"))
  @ResponseBody
  def productDetails(@PathVariable productId: Long): DeferredResult[ProductDetails] = toDeferredResult(productDetailsService.getProductDetails(productId))

}

