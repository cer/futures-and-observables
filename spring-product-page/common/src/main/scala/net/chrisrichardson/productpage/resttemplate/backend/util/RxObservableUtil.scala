package net.chrisrichardson.productpage.resttemplate.backend.util

import rx.lang.scala.{Observer, Observable}

import org.springframework.web.context.request.async.DeferredResult

object RxObservableUtil {

  implicit def toDeferredResult[T](observable : Observable[T]) = {
    val result = new DeferredResult[T]
    val onNext : T => Unit  = (x : T) => result.setResult(x)
    val onError : Throwable => Unit = (x : Throwable) => result.setErrorResult(x)

    observable.subscribe(onNext , onError)

    result
  }

}
