package net.chrisrichardson.productpage.resttemplate.backend.services.scalafutures

import scalaz.{Apply, Functor}
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object ScalazFutures {
  // Make scalaz work with futures

  implicit object FutureFunctor extends Functor[Future] {
    def fmap[A, B](r: Future[A], f: (A) => B): Future[B] = r.map(f)
  }

  implicit object FutureApply extends Apply[Future] {
    def apply[A, B](f: Future[(A) => B], a: Future[A]) = (f zip a).map {
      case (f, a) => f(a)
    }
  }

}
