package org

import mockhttpserver.core.Body
import mockhttpserver.support.JsonSupport

package object mockhttpserver extends JsonSupport{
  case class ContentType(contentType : String){
    def apply(entity : String) = Body(contentType, entity)
  }

  object Json extends ContentType("application/json"){
    def apply(entity : Any) = super.apply(toJson(entity))
  }

  object PlainText extends ContentType("text/plain")
}
