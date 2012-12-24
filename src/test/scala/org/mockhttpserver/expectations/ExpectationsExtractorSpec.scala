package org.mockhttpserver.expectations

import org.mockhttpserver.support.JunitBddSpec
import org.mockito.Mockito._
import org.mockhttpserver.core.{Get, Response}

class ExpectationsExtractorSpec extends JunitBddSpec {

  describe("Expectations Extractor"){
    val parser = mock[ExpectationsParser]
    val reader = mock[ExpectationsSourceReader]

    val path = "some path"
    val extractor = new ExpectationsExtractor(reader, parser)
    implicit val transform = mock[Transform[Get]]
    it("returns expectations from the given path"){
      val expectations = List((Get("url"), Response(200)))
      val getSource = "get source"
      when(reader.read(path)).thenReturn(Some(getSource))
      when(parser.parse(getSource)).thenReturn(expectations)
      extractor.collect(path) should be === expectations
    }

    it("does not return expectations from the given path if the source is empty"){
      when(reader.read(path)).thenReturn(None)
      extractor.collect(path) should be === List.empty
    }
  }

}
