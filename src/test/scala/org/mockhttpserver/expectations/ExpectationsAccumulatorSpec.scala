package org.mockhttpserver.expectations

import org.mockhttpserver.support.JunitBddSpec
import org.mockito.Mockito._
import org.mockhttpserver.core._
import org.mockhttpserver.core.Put
import org.mockhttpserver.core.Get
import org.mockhttpserver.core.Post
import org.mockhttpserver.core.Response
import org.mockhttpserver.expectations.Transform.{DeleteTransform, PutTransform, PostTransform, GetTransform}

class ExpectationsAccumulatorSpec extends JunitBddSpec {

  describe("Expectations Accumulator") {
    it("accumulates all types of expectations") {
      val extractor = mock[ExpectationsExtractor]
      val basePath = "base path"
      val getExpectation = (Get("get"), Response(200))
      val postExpectation = (Post("post"), Response(200))
      val putExpectation = (Put("put"), Response(200))
      val deleteExpectation = (Delete("delete"), Response(200))
      when(extractor.collect(basePath + "/get.expectations")(GetTransform)).thenReturn(List(getExpectation))
      when(extractor.collect(basePath + "/post.expectations")(PostTransform)).thenReturn(List(postExpectation))
      when(extractor.collect(basePath + "/put.expectations")(PutTransform)).thenReturn(List(putExpectation))
      when(extractor.collect(basePath + "/delete.expectations")(DeleteTransform)).thenReturn(List(deleteExpectation))
      val allExpectations = new ExpectationsAccumulator(extractor).accumulateFrom(basePath)
      allExpectations.size should be === 4
      allExpectations.contains((Get(getExpectation._1.url), getExpectation._2)) should be === true
      allExpectations.contains((Post(postExpectation._1.url), postExpectation._2)) should be === true
      allExpectations.contains((Put(putExpectation._1.url), putExpectation._2)) should be === true
      allExpectations.contains((Delete(deleteExpectation._1.url), deleteExpectation._2)) should be === true
    }
  }
}