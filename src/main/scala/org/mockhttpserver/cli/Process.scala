package org.mockhttpserver.cli

import org.mockhttpserver.core.MockHttpServer
import org.mockhttpserver.expectations.{ExpectationsAccumulator, ExpectationsParser, SourceReader, ExpectationsExtractor}
import scala.Predef._

object Process extends App {

  val conf = new Conf(args)
  val reader = new SourceReader(conf.basePath())
  val extractor = new ExpectationsExtractor(reader, new ExpectationsParser(reader))
  val accumulator = new ExpectationsAccumulator(extractor)

  def start {
    MockHttpServer(conf.host, conf.port())(accumulator.accumulate: _*).start
    println("Server Started..")
  }

  start
}
