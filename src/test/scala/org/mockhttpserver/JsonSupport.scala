package org.mockhttpserver

import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.databind.ObjectMapper
import java.io.StringWriter


trait JsonSupport {

  def toJson(obj : Any) = {
    val writer = new StringWriter
    objectMapper.writeValue(writer, obj)
    writer.toString
  }

  private lazy val objectMapper = {
    val objectMapper = new ObjectMapper
    objectMapper.registerModule(DefaultScalaModule)
    objectMapper
  }

}
