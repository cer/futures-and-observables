package net.chrisrichardson.rxaveragetradeprice.integration

import org.junit.{Assert, Test}
import rx.lang.scala.subjects.ReplaySubject
import scala.concurrent.duration._
import rx.lang.scala.Observable
import java.util.concurrent.TimeUnit
import rx.lang.scala.JavaConversions._
import scala.collection.JavaConversions._

class AmqpToObservableAdapterTest {

  val timeout = 700 milliseconds
  
  implicit def toTakeForDuration[T](s : Observable[T])  = new {
    def take(duration: Duration) : Seq[T] = toScalaObservable[T](toJavaObservable[T](s).take(timeout.toMillis, TimeUnit.MILLISECONDS)).toBlockingObservable.toList
  }

  @Test
  def shouldSendMessagesToSubscribedObserver {
    val adapter = new AmqpToObservableAdapter
    val observable = adapter.createObservable()

    val subject = ReplaySubject[String]()

    observable.subscribe(subject)

    adapter.handleMessage("hello")

    Assert.assertEquals(Seq("hello"), subject.take(timeout))

  }
 
  @Test
  def shouldSendMessagesToSubscribedObservers {
    val adapter = new AmqpToObservableAdapter
    val observable = adapter.createObservable()

    val subject1 = ReplaySubject[String]()
    val subject2 = ReplaySubject[String]()

    observable.subscribe(subject1)
    observable.subscribe(subject2)

    adapter.handleMessage("hello")

    Assert.assertEquals(Seq(("hello", "hello")), (subject1 zip subject2).take(timeout))

  }

  @Test
  def shouldUnsubscribeObservers {
    val adapter = new AmqpToObservableAdapter
    val observable = adapter.createObservable()
    val subject = ReplaySubject[String]()

    val subscription = observable.subscribe(subject)
    subscription.unsubscribe()

    adapter.handleMessage("hello")

    Assert.assertEquals(Seq(), subject.take(timeout))

  }

}
