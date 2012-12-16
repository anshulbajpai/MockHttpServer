import org.mockhttpserver.core.MockHttpServer
import org.mockhttpserver.expectations.{ExpectationsParser, ExpectationsSourceReader, ExpectationsExtractor, ExpectationsAccumulator}


val extractor = new ExpectationsExtractor(new ExpectationsSourceReader, new ExpectationsParser)
val accumulator = new ExpectationsAccumulator(extractor)
val expectations = accumulator.accumulateFrom(getClass.getResource("/expectations").toString)

MockHttpServer("localhost", 8080)(expectations:_*).start
println("Server Started..")

