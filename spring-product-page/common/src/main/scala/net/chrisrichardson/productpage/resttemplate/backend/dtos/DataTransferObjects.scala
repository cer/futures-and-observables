package net.chrisrichardson.productpage.resttemplate.backend.dtos


case class ProductInfo(productId: Long, name: String)

case class Recommendations(recommendations: Seq[ProductInfo])

case class Reviews(reviews: Seq[Review])

case class Review(rating: Int, text: String)

case class ProductDetails(productInfo: ProductInfo, recommendations: Recommendations, reviews: Reviews)
