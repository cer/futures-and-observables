package net.chrisrichardson.productpage.resttemplate.web.controllers.backendservices

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{PathVariable, ResponseBody, RequestMapping}
import net.chrisrichardson.productpage.resttemplate.backend.dtos.{Recommendations, Review, Reviews, ProductInfo}

@Controller
class BackendSimulatorController {

  @RequestMapping(Array("/productinfo/{productId}"))
  @ResponseBody
  def productDetails(@PathVariable productId: Long) = ProductInfo(productId, "Toothbrush")

  @RequestMapping(Array("/reviews/{productId}"))
  @ResponseBody
  def reviews(@PathVariable productId: Long) = Reviews(List(Review(5, "Awesome")))

  @RequestMapping(Array("/recommendations/{productId}"))
  @ResponseBody
  def recommendations(@PathVariable productId: Long) = productId match {
    case 2 => new UnsupportedOperationException("recommendations unavailable")
    case _ => Recommendations(List(ProductInfo(productId, "Rifle")))
  }


}
