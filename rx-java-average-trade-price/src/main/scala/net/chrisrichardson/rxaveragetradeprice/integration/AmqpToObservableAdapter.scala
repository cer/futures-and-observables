package net.chrisrichardson.rxaveragetradeprice.integration

import org.springframework.stereotype.Component
import rx.lang.scala.{Subject, Observable}

@Component
class AmqpToObservableAdapter {

  val subject = Subject[String]()

  val observable : Observable[String] = subject

  def createObservable() : Observable[String] = observable

  def handleMessage(message : String) {
    subject.onNext(message)
  }

}
