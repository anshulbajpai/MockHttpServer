package org.mockhttpserver.expectations

import org.mockhttpserver.core._
import org.mockhttpserver.core.Body
import scala.Some
import org.mockhttpserver.core.Response

class ExpectationsParser{

  private val Expectation = """(.+)->(.+)""".r
  private val WithBody = """(.+)\[(.+)\]@(.+)""".r
  private val WithoutBody = """(.+)""".r


  def parse[T <: Request](source : String)(implicit transform : Transform[T]) = source.split("""\*""").drop(1).foldLeft(List.empty[(T, Response)]){ (l,e) =>
    val Expectation(requestExpectation, responseExpectation) = e
      val request = requestExpectation match {
        case WithBody(url, entity, contentType) => transform.execute(url, Some(Body(contentType, entity)))
        case WithoutBody(url) => transform.execute(url)
      }
      val response = responseExpectation match {
        case WithBody(status, entity, contentType) => Response(status.toInt, Some(Body(contentType, entity)))
        case WithoutBody(status) => Response(status.toInt)
      }
      (request, response) :: l
  }
}
