package net.chrisrichardson.productpage.resttemplate.web.tests

import org.junit.Test
import org.apache.http.client.config.RequestConfig
import org.apache.http.impl.nio.client.HttpAsyncClients
import scala.concurrent._
import org.apache.http.client.methods.HttpGet
import org.apache.http.concurrent.FutureCallback
import org.apache.http.HttpResponse
import net.chrisrichardson.productpage.resttemplate.scalajson.ScalaObjectMapper
import org.apache.commons.io.IOUtils
import net.chrisrichardson.productpage.resttemplate.backend.dtos.ProductDetails
import scala.concurrent.duration._
import org.slf4j.LoggerFactory

class AsyncHttpClientExample extends AbstractWebTest {

  @Test
  def asyncHttpClient() {
    val s = jetty.makeUrl("/scalafutures/scalaz/productdetails/1")


    val requestConfig = RequestConfig.custom()
      .setSocketTimeout(3000)
      .setConnectTimeout(3000).build()

    val httpclient = HttpAsyncClients.custom()
      .setDefaultRequestConfig(requestConfig)
      .build()

    httpclient.start()

    def performAsyncRequest(url : String) = {
      val p = promise[ProductDetails]()

      val request = new HttpGet(url)

      httpclient.execute(request, new FutureCallback[HttpResponse]() {

        def completed(response: HttpResponse) {
          if (response.getStatusLine.getStatusCode != 200)
            p failure new RuntimeException("Bad status code: " + response.getStatusLine.getStatusCode)
          else
            p success (new ScalaObjectMapper).readValue(IOUtils.toString(response.getEntity.getContent), classOf[ProductDetails])
        }

        def failed(ex: Exception) {
          p.failure(ex)
        }

        def cancelled() {
          logger.warn(request.getRequestLine + " cancelled")
        }

      })
      p.future
    }

    try {
      val resultFuture = performAsyncRequest(s)

      assertSuccessful(Await.result(resultFuture, 1 seconds))

    } finally {
      httpclient.close()
    }


  }

}
