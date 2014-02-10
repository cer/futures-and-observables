package net.chrisrichardson.rxaveragetradeprice.averaging

import org.junit.{Assert, Test}
import scala.util.Random
import rx.lang.scala.Observable

import scala.concurrent.duration._

class AverageTradePriceCalculatorTest {

  val applePrices = Seq(400, 420, 410)
  val ibmPrices = Seq(30, 20, 40)
  val apple = "APPL"
  val ibm = "IBM"
  val appleTrades = Observable.from(applePrices map { Trade(apple, _)})
  val ibmTrades = Observable.from(ibmPrices map { Trade(ibm, _)})

  def average(prices : Seq[Int]) : Double = prices.reduce(_ + _) / (1.0 * prices.length)

  @Test
  def calculateAverageForApple {
    val trades  = (Observable.interval(500 milliseconds) zip appleTrades).map(_._2)

    val calculator = new AverageTradePriceCalculator
    val averages = calculator.calculateAverages(trades)

    val firstThreeAverages = averages.take(3).toBlockingObservable.toList

    Assert.assertEquals(apple, firstThreeAverages(0).symbol)
    Assert.assertEquals(average(applePrices), firstThreeAverages(0).price, 0.0)
  }

  @Test
  def calculateAverageForAppleAndIbm {
    val allTrades = (appleTrades zip ibmTrades) flatMap { case (apple, ibm) => Observable.items(apple, ibm) }
    val trades  = (Observable.interval(500 milliseconds) zip allTrades).map(_._2)

    val calculator = new AverageTradePriceCalculator
    val averages = calculator.calculateAverages(trades)

    val firstThreeAverages = averages.take(3).toBlockingObservable.toList

    firstThreeAverages(0).symbol match {
      case `apple` =>
        Assert.assertEquals(average(applePrices), firstThreeAverages(0).price, 0.0)
      case `ibm` =>
        Assert.assertEquals(average(ibmPrices), firstThreeAverages(0).price, 0.0)
    }
  }


}
