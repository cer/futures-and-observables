package net.chrisrichardson.rxaveragetradeprice.averaging

import org.junit.{Assert, Test}
import rx.lang.scala.Observable
import scala.concurrent.duration._
import org.junit.runner.RunWith
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.ContextConfiguration
import org.springframework.beans.factory.annotation.Autowired
import net.chrisrichardson.rxaveragetradeprice.integration.AmqpToObservableAdapter
import org.springframework.amqp.rabbit.core.RabbitTemplate
import net.chrisrichardson.rxaveragetradeprice.util.SerializationSupport
import org.junit.experimental.categories.Category
import net.chrisrichardson.rxaveragetradeprice.AmqpTest

@RunWith(classOf[SpringJUnit4ClassRunner])
@ContextConfiguration(locations = Array("classpath*:/appctx/*.xml"))
@Category(Array(classOf[AmqpTest]))
class AverageTradePriceCalculatorIntegrationTest {

  @Autowired
  var amqpToRxAdapter : AmqpToObservableAdapter = _

  @Autowired
  var rabbitTemplate: RabbitTemplate = _

  @Test
  def calculateAverage {

    val tradesToPublish  = Observable.interval(500 milliseconds) map { i =>

      val t = if (i % 2 == 0) -1 else +1

      val price = 400 + t * 10
      new Trade("APPL", price)
    }

    tradesToPublish.subscribe { trade => rabbitTemplate.convertAndSend("tickTock", "tickTock", SerializationSupport.writeValueAsString(trade)) }

    val trades = amqpToRxAdapter.createObservable().map { SerializationSupport.readValue(_, classOf[Trade])}

    val calculator = new AverageTradePriceCalculator
    val averages = calculator.calculateAverages(trades)

    val firstThreeAverages = averages.take(3).toBlockingObservable.toList

    Assert.assertEquals("APPL", firstThreeAverages(0).symbol)
    Assert.assertEquals(400.0, firstThreeAverages(0).price, 10)

  }

}
