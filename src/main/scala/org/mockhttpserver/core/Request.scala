package org.mockhttpserver.core

case class Body(contentType : String, entity : Any)

trait Request{
 def url : String
 def body : Option[Body]
}

case class Response(status : Int, body : Option[Body] = None)

case class Get(url : String) extends Request {
  val body = None
}
case class Delete(url : String) extends Request {
  val body = None
}

case class Post(url : String, body : Option[Body] = None) extends Request
case class Put(url : String, body : Option[Body]= None) extends Request
