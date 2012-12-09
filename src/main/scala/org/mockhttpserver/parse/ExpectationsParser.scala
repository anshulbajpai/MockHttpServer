package org.mockhttpserver.parse

import org.mockhttpserver.core.{Body, Response, Request}

object ExpectationsParser {

  val Expectation = """(.+)->(.+)""".r
  val WithBody = """(.+)\[(.+)\]@(.+)""".r
  val WithoutBody = """(.+)""".r

  def parse(source : String) = source.split("""\*""").drop(1).foldLeft(List.empty[(Request, Response)]){ (l,e) =>
      val Expectation(requestExpectation, responseExpectation) = e
      val request = requestExpectation match {
        case WithBody(url, entity, contentType) => Request(url, Some(Body(contentType, entity)))
        case WithoutBody(url) => Request(url)
      }
      val response = responseExpectation match {
        case WithBody(status, entity, contentType) => Response(status.toInt, Some(Body(contentType, entity)))
        case WithoutBody(status) => Response(status.toInt)
      }
      (request, response) :: l
  }
}
