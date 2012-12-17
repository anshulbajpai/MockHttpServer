package org.mockhttpserver.cli

import org.mockhttpserver.core.MockHttpServer
import org.mockhttpserver.expectations.{ExpectationsAccumulator, ExpectationsParser, ExpectationsSourceReader, ExpectationsExtractor}
import scala.Predef._

object Start extends App {
  val conf = new Conf(args)
  val extractor = new ExpectationsExtractor(new ExpectationsSourceReader, new ExpectationsParser)
  val accumulator = new ExpectationsAccumulator(extractor)
  val expectations = accumulator.accumulateFrom(conf.basePath())

  MockHttpServer(conf.host, conf.port())(expectations:_*).start
  println("Server Started..")

}
