package org.mockhttpserver.core

case class Request(url : String, body : Option[Body] = None)
case class Response(status : Int, body : Option[Body] = None)

object GET{
  def apply(url : String) = Request(url)
}

object POST{
  def apply(url : String, body : Option[Body]) = Request(url, body)
}

case class Body(contentType : String, entity : Any)