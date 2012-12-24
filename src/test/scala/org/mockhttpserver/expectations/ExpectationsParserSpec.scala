package org.mockhttpserver.expectations

import org.mockhttpserver.support.JunitBddSpec
import org.mockhttpserver.core.{Post, Response}
import org.mockhttpserver.{PlainText, Json}

class ExpectationsParserSpec extends JunitBddSpec{

  case class Entity(name : String, age : String)

  describe("Expectations Parser"){
    it("fetches expectations correctly"){
      val expectations = new ExpectationsParser().parse(classPathResource("/expectations/post.expectations"))(Transform.PostTransform)
      expectations.size should be(5)

      expectations should contain (Post("/foo1"), Response(204))
      expectations should contain (Post("/foo2", Some(Json(Entity("request", "10")))), Response(200))
      expectations should contain (Post("/foo3"), Response(200, Some(Json(Entity("response", "10")))))
      expectations should contain (Post("/foo4", Some(Json(Entity("request", "10")))), Response(200, Some(Json(Entity("response", "10")))))
      expectations should contain (Post("/foo5", Some(PlainText("hi"))), Response(200, Some(PlainText("bye"))))
    }
  }

}
