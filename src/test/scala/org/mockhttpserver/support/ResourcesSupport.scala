package org.mockhttpserver.support

import scala.io.Source

trait ResourcesSupport {

  def classPathResource(path : String) = Source.fromURL(getClass.getResource(path)).getLines.mkString("\n")

}
