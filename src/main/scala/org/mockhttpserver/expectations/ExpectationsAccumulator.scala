package org.mockhttpserver.expectations

import org.mockhttpserver.core._
import org.mockhttpserver.expectations.Transform._

class ExpectationsAccumulator(extractor : ExpectationsExtractor) {

  private def extract[T <: Request](path : String)(implicit basePath : String, transform : Transform[T])  = {
    extractor.collect[T](basePath + path)
  }

  private val extractGet = {implicit s : String => extract[Get]("/get.expectations")}
  private val extractPost = {implicit s : String => extract[Post]("/post.expectations")}
  private val extractPut = {implicit s : String => extract[Put]("/put.expectations")}
  private val extractDelete = {implicit s : String => extract[Delete]("/delete.expectations")}

  private val extractAll = List(extractGet, extractPost, extractPut, extractDelete)

  def accumulateFrom(basePath : String) = extractAll.foldRight(List.empty[(Request, Response)]){ _(basePath) ::: _ }

}