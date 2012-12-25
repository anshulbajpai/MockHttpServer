package org.mockhttpserver.expectations

import org.mockhttpserver.core._
import org.mockhttpserver.expectations.Transform._

class ExpectationsAccumulator(extractor : ExpectationsExtractor) {

  private def extract[T <: Request](path : String)(implicit transform : Transform[T])  = extractor.collect[T](path)

  private def extractGet = extract[Get]("/get.expectations")
  private def extractPost =  extract[Post]("/post.expectations")
  private def extractPut = extract[Put]("/put.expectations")
  private def extractDelete = extract[Delete]("/delete.expectations")

  private val extractAll = List(extractGet _, extractPost _, extractPut _, extractDelete _)

  def accumulate = extractAll.foldRight(List.empty[(Request, Response)]){ _() ::: _ }

}