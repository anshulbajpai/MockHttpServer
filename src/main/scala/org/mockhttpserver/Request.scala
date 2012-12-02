package org.mockhttpserver

class Request(url : String)


object GET{
  def apply(url : String) = new Request(url)
}