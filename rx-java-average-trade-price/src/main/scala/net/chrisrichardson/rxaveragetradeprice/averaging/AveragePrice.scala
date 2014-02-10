package net.chrisrichardson.rxaveragetradeprice.averaging

case class AveragePrice(symbol : String, price : Double, prices : Seq[Double])