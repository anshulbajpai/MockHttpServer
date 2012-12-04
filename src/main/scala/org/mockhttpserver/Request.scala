package org.mockhttpserver

case class Request(url : String)


object GET{
  def apply(url : String) = Request(url)
}

object POST{
  def apply(url : String) = Request(url)
}