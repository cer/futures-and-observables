package net.chrisrichardson.productpage.resttemplate.web.tests

import org.junit._

import net.chrisrichardson.productpage.resttemplate.backend.dtos.ProductDetails

class ProductControllerWebTest extends AbstractWebTest {


  @Test
  def productDetailsUsingJavaFutures() {
    val s = jetty.makeUrl("/javafutures/productdetails/1")
    val response = restTemplate.getForObject(s, classOf[ProductDetails])
    assertSuccessful(response)
  }
  @Test
  def productDetailsUsingHystrix() {
    val s = jetty.makeUrl("/javafutures/hystrix/productdetails/1")
    val response = restTemplate.getForObject(s, classOf[ProductDetails])
    assertSuccessful(response)
  }

  @Test
  def asyncProductInfo() {
    val s = jetty.makeUrl("/scalafutures/productdetails/1")
    val response = restTemplate.getForObject(s, classOf[ProductDetails])
    assertSuccessful(response)
  }

  @Test
  def productDetailsUsingAsyncRestTemplate() {
    val s = jetty.makeUrl("/asyncresttemplate/productdetails/1")
    val response = restTemplate.getForObject(s, classOf[ProductDetails])
    assertSuccessful(response)
  }

  @Test
  def scalazController() {
    val s = jetty.makeUrl("/scalafutures/scalaz/productdetails/1")
    val response = restTemplate.getForObject(s, classOf[ProductDetails])
    assertSuccessful(response)
  }

  @Test
  def recommendationsUnavailable() {
    val s = jetty.makeUrl("/scalafutures/scalaz/productdetails/2")
    val response = restTemplate.getForObject(s, classOf[ProductDetails])
    assertSuccessful(response)
  }


  @Test
  def rxController() {
    val s = jetty.makeUrl("rx/productdetails/1")
    val response = restTemplate.getForObject(s, classOf[ProductDetails])
    assertSuccessful(response)
  }


}
