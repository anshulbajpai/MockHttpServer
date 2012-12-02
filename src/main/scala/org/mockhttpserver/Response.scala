package org.mockhttpserver


case class Response(status : Int, contentType : String)(val body :Any)