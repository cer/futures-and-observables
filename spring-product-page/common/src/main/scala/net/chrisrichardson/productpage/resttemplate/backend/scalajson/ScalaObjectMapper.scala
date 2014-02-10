package net.chrisrichardson.productpage.resttemplate.scalajson

import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.databind.ObjectMapper

class ScalaObjectMapper extends ObjectMapper {

  registerModule(DefaultScalaModule)

}
