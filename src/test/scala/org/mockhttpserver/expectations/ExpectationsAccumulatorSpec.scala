package org.mockhttpserver.expectations

import org.mockhttpserver.support.BddSpec
import org.mockito.Mockito._
import org.mockhttpserver.core._
import org.mockhttpserver.core.Request
import org.mockhttpserver.core.Response

class ExpectationsAccumulatorSpec extends BddSpec {

  describe("Expectations Accumulator") {
    it("accumulates all types of expectations") {
      val extractor = mock[ExpectationsExtractor]
      val basePath = "base path"
      val getExpectation = (Request("get"), Response(200))
      val postExpectation = (Request("post"), Response(200))
      val putExpectation = (Request("put"), Response(200))
      val deleteExpectation = (Request("delete"), Response(200))
      when(extractor.collect(basePath + "/get.expectations")).thenReturn(List(getExpectation))
      when(extractor.collect(basePath + "/post.expectations")).thenReturn(List(postExpectation))
      when(extractor.collect(basePath + "/put.expectations")).thenReturn(List(putExpectation))
      when(extractor.collect(basePath + "/delete.expectations")).thenReturn(List(deleteExpectation))
      val allExpectations = new ExpectationsAccumulator(extractor).accumulateFrom(basePath)
      allExpectations.size should be === 4
      allExpectations should contain((GET(getExpectation._1.url), getExpectation._2))
      allExpectations should contain((POST(postExpectation._1.url, postExpectation._1.body), postExpectation._2))
      allExpectations should contain((PUT(putExpectation._1.url, putExpectation._1.body), putExpectation._2))
      allExpectations should contain((DELETE(deleteExpectation._1.url), deleteExpectation._2))
    }
  }
}