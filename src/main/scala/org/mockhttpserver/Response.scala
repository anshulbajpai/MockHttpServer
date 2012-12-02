package org.mockhttpserver

import org.apache.http.entity.ContentType

class Response(status : Int, contentType : ContentType, body :String) {

}

object Response{
  def apply(status : Int, contentType : ContentType)(body :String) = new Response(status, contentType, body)
}
