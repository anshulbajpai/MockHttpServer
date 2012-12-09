package org.mockhttpserver.expectations

import org.mockhttpserver.core._


class ExpectationsAccumulator(extractor : ExpectationsExtractor) {

  type ReqRep = (Request, Response)
  type Transform = (ReqRep) => ReqRep
  private def extract(path : String)(transform : Transform) = extractor.collect(path).map(transform)

  private def extractGet= {s : String => extract(s + "/get.expectations"){ e => (GET(e._1.url), e._2)}}
  private def extractPost = {s : String => extract(s + "/post.expectations"){ e => (POST(e._1.url, e._1.body), e._2)}}
  private def extractPut = {s : String => extract(s+ "/put.expectations"){ e => (PUT(e._1.url, e._1.body), e._2) }}
  private val extractDelete = {s : String => extract(s + "/delete.expectations"){e => (DELETE(e._1.url), e._2)}}

  private val extractAll = List(extractGet, extractPost, extractPut, extractDelete)

  def accumulateFrom (basePath : String) = extractAll.foldRight(List.empty[ReqRep]){ _(basePath) ::: _ }
}
