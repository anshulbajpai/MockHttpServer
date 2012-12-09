package org.mockhttpserver.expectations

import org.mockhttpserver.support.BddSpec
import org.mockito.Mockito._
import org.mockhttpserver.core.{Response, Request}

class ExpectationsExtractorSpec extends BddSpec {

  describe("Expectations Extractor"){

    it("returns expectations from the given path"){
      val path = "some path"
      val parser = mock[ExpectationsParser]
      val reader = mock[ExpectationsSourceReader]
      val expectations = List((Request("url"), Response(200)))
      val getSource = "get source"
      when(reader.read(path)).thenReturn(Some(getSource))
      when(parser.parse(getSource)).thenReturn(expectations)
      new ExpectationsExtractor(reader, parser).collect(path) should be(expectations)
    }

    it("does not return expectations from the given path if the source is empty"){
      val path = "some path"
      val parser = mock[ExpectationsParser]
      val reader = mock[ExpectationsSourceReader]
      when(reader.read(path)).thenReturn(None)
      new ExpectationsExtractor(reader, parser).collect(path) should be(List.empty)
    }
  }

}
