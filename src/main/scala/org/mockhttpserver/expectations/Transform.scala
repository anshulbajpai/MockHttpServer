package org.mockhttpserver.expectations

import org.mockhttpserver.core._
import org.mockhttpserver.core.Put
import org.mockhttpserver.core.Body
import org.mockhttpserver.core.Get
import org.mockhttpserver.core.Post
import org.mockhttpserver.core.Delete

trait Transform[T <: Request] {
  def execute(url : String, body : Option[Body] = None) : T
}

object Transform {
  implicit object GetTransform extends Transform[Get]{
    def execute(url: String, body : Option[Body]) = Get(url)
  }

  implicit object PostTransform extends Transform[Post]{
    def execute(url: String, body : Option[Body]) = Post(url, body)
  }

  implicit object PutTransform extends Transform[Put]{
    def execute(url: String, body : Option[Body]) = Put(url, body)
  }

  implicit object DeleteTransform extends Transform[Delete]{
    def execute(url: String, body : Option[Body]) = Delete(url)
  }
}