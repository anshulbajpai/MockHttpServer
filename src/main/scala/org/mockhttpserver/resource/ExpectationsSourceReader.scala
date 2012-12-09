package org.mockhttpserver.resource

import io.Source
import java.io.FileNotFoundException

class ExpectationsSourceReader {
  def read(path : String) : Option[String] = {
    try {
      Some(Source.fromURL(path).getLines.mkString)
    }
    catch {
      case e : FileNotFoundException => None
    }
  }
}
