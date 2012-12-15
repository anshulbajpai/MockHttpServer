package org.mockhttpserver.expectations

import org.mockhttpserver.core._
import scala.Some


class ExpectationsExtractor(sourceReader : ExpectationsSourceReader, parser : ExpectationsParser) {

  def collect[T <: Request](path: String)(implicit transform : Transform[T]) = sourceReader.read(path) match {
      case Some(s) => parser.parse(s)
      case _ => List.empty
  }
}
