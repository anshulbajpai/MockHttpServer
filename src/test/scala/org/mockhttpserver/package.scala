package org

import mockhttpserver.core.Body

package object mockhttpserver {
  case class ContentType(contentType : String){
    def apply(entity : String) = Body(contentType, entity)
  }

  object Json extends ContentType("application/json")
  object PlainText extends ContentType("text/plain")
}
