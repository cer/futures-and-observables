package net.chrisrichardson.productpage.resttemplate.backend.util

import org.springframework.util.concurrent.{ListenableFutureCallback, ListenableFuture}
import scala.concurrent._
import rx.lang.scala.{Subscription, Observable, Observer}
import org.slf4j.LoggerFactory

object ListenableFutureUtil {

  val logger = LoggerFactory.getLogger(this.getClass)

  implicit def toScalaFuture[T](f: ListenableFuture[T]) = {
    logger.debug("converting to scala future")
    val p = promise[T]
    f.addCallback(new ListenableFutureCallback[T] {
      def onSuccess(result: T) {
        p.success(result)
      }

      def onFailure(t: Throwable) {
        p.failure(t)
      }
    })
    p.future
  }

  implicit def toRxObservable[T](future: ListenableFuture[T]) : Observable[T] = {
    def create(o: Observer[T]) = {
      future.addCallback(new ListenableFutureCallback[T] {
        def onSuccess(result: T) {
          o.onNext(result)
          o.onCompleted()
        }

        def onFailure(t: Throwable) {
          o.onError(t)
        }
      })
      Subscription({})
    }

    Observable.create(create _)
  }

}
