package org.mockhttpserver.parse

import org.mockhttpserver.support.BddSpec
import org.mockhttpserver.core.{Response, Request}
import org.mockhttpserver.{PlainText, Json}

class ExpectationsParserSpec extends BddSpec{

  describe("Expectations Parser"){
    it("fetches expectations correctly"){
      val expectations = ExpectationsParser.parse(classPathResource("/expectations/post.txt"))
      expectations.size should be(5)
      expectations should contain((Request("/foo1"), Response(204)))
      expectations should contain((Request("/foo2", Some(Json("""{"name":"request","age":"10"}"""))), Response(200)))
      expectations should contain((Request("/foo3"), Response(200, Some(Json("""{"name":"response","age":"10"}""")))))
      expectations should contain((Request("/foo4", Some(Json("""{"name":"request","age":"10"}"""))), Response(200, Some(Json("""{"name":"response","age":"10"}""")))))
      expectations should contain((Request("/foo5", Some(PlainText("hi"))), Response(200, Some(PlainText("bye")))))
    }
  }

}
