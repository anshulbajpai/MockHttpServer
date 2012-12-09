package org.mockhttpserver.resource

import org.mockhttpserver.support.BddSpec
import org.mockhttpserver.parser.ExpectationsParser
import org.mockito.Mockito._
import org.mockhttpserver.core.{Response, Request}

class ExpectationsCollectorSpec extends BddSpec {

  describe("Expectations Collector"){

    it("returns get expectations from the given path"){
      val path = "some path"
      val parser = mock[ExpectationsParser]
      val reader = mock[ExpectationsSourceReader]
      val getExpectations = List((Request("url"), Response(200)))
      val getSource = "get source"
      when(reader.read(path + "/get.expectations")).thenReturn(Some(getSource))
      when(parser.parse(getSource)).thenReturn(getExpectations)
      new ExpectationsCollector(reader, parser).collect(path) should be(getExpectations)
    }

    it("does not returns get expectations from the given path if the source is empty"){
      val path = "some path"
      val parser = mock[ExpectationsParser]
      val reader = mock[ExpectationsSourceReader]
      when(reader.read(path + "/get.expectations")).thenReturn(None)
      new ExpectationsCollector(reader, parser).collect(path) should be(List.empty)
    }
  }

}
