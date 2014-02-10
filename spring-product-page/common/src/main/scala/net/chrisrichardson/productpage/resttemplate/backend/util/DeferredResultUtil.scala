package net.chrisrichardson.productpage.resttemplate.backend.util

import org.springframework.web.context.request.async.DeferredResult
import rx.{Observer, Observable}
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object DeferredResultUtil {

  def subscribeTo[T](deferredResult : DeferredResult[T], observable : Observable[T]) {
    observable.subscribe(new Observer[T] {
      def onCompleted() {}

      def onError(e: Throwable) {
        deferredResult.setErrorResult(e)
      }

      def onNext(r: T) {
        deferredResult.setResult(r)
      }
    })
  }

  implicit def toPipeableObservable[T](observable : Observable[T]) = new {
    def pipeTo(deferredResult : DeferredResult[T]) {
      observable.subscribe(new Observer[T] {
        def onCompleted() {}

        def onError(e: Throwable) {
          deferredResult.setErrorResult(e)
        }

        def onNext(r: T) {
          deferredResult.setResult(r)
        }
      })
    }
  }

  def toDeferredResult[T](f: Future[T]) = {
    val result = new DeferredResult[T]

    f onSuccess {
      case r => result.setResult(r)
    }
    f onFailure {
      case t => result.setErrorResult(t)
    }

    result
  }


}
