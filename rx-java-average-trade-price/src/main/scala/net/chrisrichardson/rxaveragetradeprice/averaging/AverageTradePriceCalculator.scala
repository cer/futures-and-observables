package net.chrisrichardson.rxaveragetradeprice.averaging

import rx.lang.scala.Observable
import scala.concurrent.duration._

class AverageTradePriceCalculator {

  def calculateAverages(trades: Observable[Trade]): Observable[AveragePrice] = {
    trades.groupBy(_.symbol).map { p =>

        val (symbol, tradesForSymbol) = p


        val openingEverySecond = Observable.items(-1L) ++ Observable.interval(1 seconds)

        def closingAfterSixSeconds(opening: Any) = Observable.interval(6 seconds).take(1)

        tradesForSymbol.window(openingEverySecond, closingAfterSixSeconds _).map {
          windowOfTradesForSymbol =>
            windowOfTradesForSymbol.foldLeft((0.0, 0, List[Double]())) { (soFar, trade) =>
                val (sum, count, prices) = soFar
                (sum + trade.price, count + 1, trade.price +: prices)
            } map { x =>
                val (sum, length, prices) = x
                AveragePrice(symbol, sum / length, prices)
            }
        }.flatten

    }.flatten
  }

}
