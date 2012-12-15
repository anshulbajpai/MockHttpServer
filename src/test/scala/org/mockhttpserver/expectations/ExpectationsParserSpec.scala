package org.mockhttpserver.expectations

import org.mockhttpserver.support.BddSpec
import org.mockhttpserver.core.{Post, Response}
import org.mockhttpserver.{PlainText, Json}

class ExpectationsParserSpec extends BddSpec{

  describe("Expectations Parser"){
    it("fetches expectations correctly"){
      val expectations = new ExpectationsParser().parse(classPathResource("/expectations/post.expectations"))(Transform.PostTransform)
      expectations.size should be(5)

      expectations should contain (Post("/foo1"), Response(204))
      expectations should contain (Post("/foo2", Some(Json("""{"name":"request","age":"10"}"""))), Response(200))
      expectations should contain (Post("/foo3"), Response(200, Some(Json("""{"name":"response","age":"10"}"""))))
      expectations should contain (Post("/foo4", Some(Json("""{"name":"request","age":"10"}"""))), Response(200, Some(Json("""{"name":"response","age":"10"}"""))))
      expectations should contain (Post("/foo5", Some(PlainText("hi"))), Response(200, Some(PlainText("bye"))))
    }
  }

}
