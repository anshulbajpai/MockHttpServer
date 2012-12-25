package org.mockhttpserver.expectations

import io.Source
import java.io.FileNotFoundException

class SourceReader(basePath : String) {
  def read(path : String) : Option[String] = {
    try {
      Some(Source.fromFile(basePath + path).getLines.mkString("\n"))
    }
    catch {
      case e : FileNotFoundException => None
    }
  }
}
