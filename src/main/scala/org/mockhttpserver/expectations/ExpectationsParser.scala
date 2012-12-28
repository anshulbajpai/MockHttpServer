package org.mockhttpserver.expectations

import org.mockhttpserver.core.{Body, Response, Request}
import scala.util.parsing.combinator._

class ExpectationsParser(reader : SourceReader) extends JavaTokenParsers{

  def url = "/.+~".r ^^ {case url => url.init}
  def entity(implicit `type` : String) = fileEntity | inlineEntity
  def fileEntity(implicit `type` : String) = "file("~"[^)]*".r~")" ^^ { case "file("~fileName~")" => readFrom("/%s/%s.%s".format(`type`, fileName, `type`))}
  def inlineEntity = "[^|]+".r
  def contentType = "[^-\n]+".r

  def body(implicit `type` : String) = "|"~entity~"|@"~contentType ^^ {case "|"~entity~"|@"~contentType => Body(contentType, entity)}
  def response = wholeNumber~opt(body("response")) ^^ {case status~body => Response(status.toInt,body)}

  def parse[T <: Request](source: String)(implicit transform: Transform[T]): List[(T, Response)] = {
    def request = url~opt(body("request")) ^^ {case url~body => transform.execute(url, body)}
    def expectation = "*"~request~"->"~response ^^ {case "*"~request~"->"~response => (request, response)}
    def expectations = rep(expectation)
    parseAll(expectations, source).get
  }

  private def readFrom(fileName: String) = reader.read(fileName).getOrElse("")
}
