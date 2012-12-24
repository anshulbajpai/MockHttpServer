package org.mockhttpserver.core

import org.simpleframework.http

trait RequestExtractor{
  def method : String
  def transform(req : http.Request) : Some[Request]
  def unapply(req : http.Request) : Option[Request] = if(req.getMethod == method) transform(req)  else  None

  def getBody(req : http.Request) = req.getContent match {
    case c if !c.isEmpty=> Some(Body(req.getContentType.toString, c))
    case _ => None
  }
}

object GET extends RequestExtractor{
  val method = "GET"
  def transform(req : http.Request) = Some(Get(req.getTarget))
}

object DELETE extends  RequestExtractor{
  val method = "DELETE"
  def transform(req : http.Request) = Some(Delete(req.getTarget))
}

object POST extends  RequestExtractor{
  val method = "POST"
  def transform(req : http.Request) = Some(Post(req.getTarget, getBody(req)))
}

object PUT  extends RequestExtractor{
  val method = "PUT"
  def transform(req : http.Request) = Some(Put(req.getTarget, getBody(req)))
}

