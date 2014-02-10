package net.chrisrichardson.productpage.resttemplate.backend.services.rx

import org.springframework.stereotype.Component

import net.chrisrichardson.productpage.resttemplate.backend.dtos.{Reviews, Review}
import rx.lang.scala.Observable

@Component
class ReviewService {


  def getReviews_rx(productId: Long) : Observable[Reviews] = Observable.items(Reviews(List(Review(5, "Awesome"))))

}

