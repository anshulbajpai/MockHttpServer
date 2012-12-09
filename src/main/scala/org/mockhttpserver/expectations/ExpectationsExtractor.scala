package org.mockhttpserver.expectations

class ExpectationsExtractor(sourceReader : ExpectationsSourceReader, parser : ExpectationsParser) {
  def collect(path: String) = sourceReader.read(path) match {
      case Some(s) => parser.parse(s)
      case _ => List.empty
  }
}
