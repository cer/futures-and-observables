package net.chrisrichardson.rxaveragetradeprice.util

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule

object SerializationSupport extends ObjectMapper {

  registerModule(DefaultScalaModule)
}
