package org.mockhttpserver.expectations

import org.mockhttpserver.core._


class ExpectationsAccumulator(extractor : ExpectationsExtractor) {

  type Transform = ((Request, Response)) => (Request, Response)
  private def extract(path : String)(transform : Transform) = extractor.collect(path).map(transform)

  private def extractGet(basePath : String) = extract(basePath + "/get.expectations"){ e => (GET(e._1.url), e._2) }
  private def extractPost(basePath : String) = extract(basePath + "/post.expectations"){ e => (POST(e._1.url, e._1.body), e._2) }
  private def extractPut(basePath : String) = extract(basePath + "/put.expectations"){ e => (PUT(e._1.url, e._1.body), e._2) }
  private def extractDelete(basePath : String) = extract(basePath + "/delete.expectations"){ e => (DELETE(e._1.url), e._2) }

  private val extractAll = List(extractGet _, extractPost _, extractPut _, extractDelete _)

  def accumulateFrom (basePath : String) = extractAll.foldRight(List.empty[(Request, Response)]){ _(basePath) ::: _ }
}
