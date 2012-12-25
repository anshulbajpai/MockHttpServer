package org.mockhttpserver.expectations

import org.mockhttpserver.core._
import org.mockhttpserver.core.Body
import scala.Some
import org.mockhttpserver.core.Response

class ExpectationsParser(sourceReader : SourceReader){

  private val Expectation = """(.+)->(.+)\n{0,1}""".r
  private val WithInlineBody = """(.+)\|(.+)\|@(.+)""".r
  private val WithExternalizedBody = """(.+)\|file\((.+)\)\|@(.+)""".r
  private val WithoutBody = """(.+)""".r


  def parse[T <: Request](source : String)(implicit transform : Transform[T]) = source.split("""\*""").drop(1).foldLeft(List.empty[(T, Response)]){ (l,e) =>
    val Expectation(requestExpectation, responseExpectation) = e
      val request = requestExpectation match {
        case WithExternalizedBody(url, fileName, contentType) => transform.execute(url, Some(Body(contentType, getEntityFrom("/request/" + fileName + ".request"))))
        case WithInlineBody(url, entity, contentType) => transform.execute(url, Some(Body(contentType, entity)))
        case WithoutBody(url) => transform.execute(url)
      }
      val response = responseExpectation match {
        case WithExternalizedBody(status, fileName, contentType) => Response(status.toInt, Some(Body(contentType, getEntityFrom("/response/" + fileName + ".response"))))
        case WithInlineBody(status, entity, contentType) => Response(status.toInt, Some(Body(contentType, entity)))
        case WithoutBody(status) => Response(status.toInt)
      }
      (request, response) :: l
  }

  private def getEntityFrom(fileName: String) = sourceReader.read(fileName).getOrElse("")
}
