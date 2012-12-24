package org.mockhttpserver.expectations

import io.Source
import java.io.FileNotFoundException

class SourceReader(basePath : String) {
  def read(path : String) : Option[String] = {
    try {
      Some(Source.fromURL(basePath + path).getLines.mkString)
    }
    catch {
      case e : FileNotFoundException => None
    }
  }
}
