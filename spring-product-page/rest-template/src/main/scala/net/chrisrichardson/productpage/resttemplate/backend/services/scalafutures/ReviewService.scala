package net.chrisrichardson.productpage.resttemplate.backend.services.scalafutures

import org.springframework.stereotype.Component

import org.springframework.scheduling.annotation.{AsyncResult, Async}
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import rx.Observable
import net.chrisrichardson.productpage.resttemplate.backend.dtos.{Reviews, Review}

@Component("ReviewService_scalaFuture")
class ReviewService {

  def getReviews(productId: Long) = Future {
    Reviews(List(Review(5, "Awesome")))
  }

}

