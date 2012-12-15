package org.mockhttpserver.expectations

import org.mockhttpserver.core._
import org.mockhttpserver.expectations.Transform._

class ExpectationsAccumulator(extractor : ExpectationsExtractor) {

  private def extract[T <: Request](path : String)(implicit transform : Transform[T])  = extractor.collect[T](path)

  private val extractGet = {s : String => extract[Get](s + "/get.expectations")}
  private val extractPost = {s : String => extract[Post](s + "/post.expectations")}
  private val extractPut = {s : String => extract[Put](s+ "/put.expectations")}
  private val extractDelete = {s : String => extract[Delete](s + "/delete.expectations")}

  private val extractAll = List(extractGet, extractPost, extractPut, extractDelete)

  def accumulateFrom(basePath : String) = extractAll.foldRight(List.empty[(Request, Response)]){ _(basePath) ::: _ }

}