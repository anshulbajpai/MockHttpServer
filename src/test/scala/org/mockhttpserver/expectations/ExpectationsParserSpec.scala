package org.mockhttpserver.expectations

import org.mockhttpserver.support.JunitBddSpec
import org.mockhttpserver.core.{Body, Put, Post, Response}
import org.mockhttpserver.{PlainText, Json}
import org.mockito.Mockito._

class ExpectationsParserSpec extends JunitBddSpec{

  case class Entity(name : String, age : String)

  describe("Expectations Parser"){
    val reader = mock[SourceReader]
    val parser = new ExpectationsParser(reader)
    it("fetches inline expectations correctly"){

      val expectations = parser.parse(classPathResource("/expectations/post.expectations"))(Transform.PostTransform)
      expectations.size should be(5)

      expectations should contain (Post("/foo1"), Response(204))
      expectations should contain (Post("/foo2", Some(Json(Entity("request", "10")))), Response(200))
      expectations should contain (Post("/foo3"), Response(200, Some(Json(Entity("response", "10")))))
      expectations should contain (Post("/foo4", Some(Json(Entity("request", "10")))), Response(200, Some(Json(Entity("response", "10")))))
      expectations should contain (Post("/foo5", Some(PlainText("hi"))), Response(200, Some(PlainText("bye"))))
    }

    it("fetches externalized expectations correctly"){
      when(reader.read("/request/putFoo4.request")).thenReturn(Some("foobar request"))
      when(reader.read("/response/putFoo4.response")).thenReturn(Some("foobar response"))
      val expectations = parser.parse(classPathResource("/expectations/put.expectations"))(Transform.PutTransform)
      expectations.size should be(1)
      expectations should contain (Put("/foo4", Some(Body("text/xml","foobar request"))), Response(200,Some(Body("text/xml","foobar response"))))
    }
  }

}
