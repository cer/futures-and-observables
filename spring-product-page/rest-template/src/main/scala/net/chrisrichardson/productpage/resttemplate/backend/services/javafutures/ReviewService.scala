package net.chrisrichardson.productpage.resttemplate.backend.services.javafutures

import org.springframework.stereotype.Component

import org.springframework.scheduling.annotation.{AsyncResult, Async}
import net.chrisrichardson.productpage.resttemplate.backend.dtos.{Reviews, Review}

@Component("reviewService_javaFuture")
class ReviewService {

  @Async
  def getReviews(productId: Long): java.util.concurrent.Future[Reviews] = {
    new AsyncResult(Reviews(List(Review(5, "Awesome"))))
  }

}

