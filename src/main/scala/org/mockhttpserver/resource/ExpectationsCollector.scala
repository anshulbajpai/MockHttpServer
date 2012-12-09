package org.mockhttpserver.resource

import org.mockhttpserver.parser.ExpectationsParser

class ExpectationsCollector(sourceReader : ExpectationsSourceReader, parser : ExpectationsParser) {
  def collect(path: String) = sourceReader.read(path + "/get.expectations") match {
      case Some(s) => parser.parse(s)
      case _ => List.empty
  }

}
