package net.chrisrichardson.rxaveragetradeprice.integration

import org.junit.runner.RunWith
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.junit.{Assert, Test}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.amqp.rabbit.core.RabbitTemplate
import scala.concurrent._
import scala.concurrent.duration._
import org.junit.experimental.categories.Category
import net.chrisrichardson.rxaveragetradeprice.AmqpTest

@RunWith(classOf[SpringJUnit4ClassRunner])
@ContextConfiguration(locations = Array("classpath*:/appctx/*.xml"))
@Category(Array(classOf[AmqpTest]))
class AmqpToObservableAdapterIntegrationTest {

  @Autowired
  var adapter: AmqpToObservableAdapter = _

  @Autowired
  var rabbitTemplate: RabbitTemplate = _


  @Test
  def shouldCreateObservableContainingMessages {

    val observable  = adapter.createObservable()

    val messages = Seq.fill(20) { "m" + System.currentTimeMillis()}

    val p = promise[Seq[String]]()

    observable.filter { messages.contains _}.take(messages.length).toSeq.subscribe { p.success(_) }

    messages.foreach { m => rabbitTemplate.convertAndSend("tickTock", "tickTock", m)}

    Assert.assertEquals(messages, Await.result(p.future, 5 seconds))
  }
}
