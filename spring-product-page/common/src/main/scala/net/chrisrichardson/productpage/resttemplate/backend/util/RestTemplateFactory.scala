package net.chrisrichardson.productpage.resttemplate.backend.util

import org.springframework.web.client.{RestTemplate, AsyncRestTemplate}
import org.springframework.http.client.HttpComponentsAsyncClientHttpRequestFactory
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import scala.collection.JavaConversions._
import net.chrisrichardson.productpage.resttemplate.scalajson.ScalaObjectMapper

object  RestTemplateFactory {

  def makeAsyncRestTemplate() = {
    val asyncRestTemplate = new AsyncRestTemplate(new HttpComponentsAsyncClientHttpRequestFactory())
    asyncRestTemplate.getMessageConverters foreach {
      case mc: MappingJackson2HttpMessageConverter => mc.setObjectMapper(new ScalaObjectMapper)
      case _ =>
    }
    asyncRestTemplate
  }
  def makeRestTemplate() = {
    val restTemplate = new RestTemplate()
    restTemplate.getMessageConverters foreach {
      case mc: MappingJackson2HttpMessageConverter => mc.setObjectMapper(new ScalaObjectMapper)
      case _ =>
    }
    restTemplate
  }

}
