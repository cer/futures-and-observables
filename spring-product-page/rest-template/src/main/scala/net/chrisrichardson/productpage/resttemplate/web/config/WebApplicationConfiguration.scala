package net.chrisrichardson.productpage.resttemplate.web.config

import org.springframework.context.annotation.{ComponentScan, Configuration}
import org.springframework.web.servlet.config.annotation.{WebMvcConfigurationSupport, EnableWebMvc}
import java.util
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import net.chrisrichardson.productpage.resttemplate.scalajson.ScalaObjectMapper

@Configuration
@EnableWebMvc
@ComponentScan(Array("net.chrisrichardson.productpage.resttemplate.web"))
class WebApplicationConfiguration extends WebMvcConfigurationSupport {


  override def configureMessageConverters(converters: util.List[HttpMessageConverter[_]]) {
    val jsonConvertor = new MappingJackson2HttpMessageConverter()
    jsonConvertor.setObjectMapper(new ScalaObjectMapper)
    converters.add(jsonConvertor)
  }

}
