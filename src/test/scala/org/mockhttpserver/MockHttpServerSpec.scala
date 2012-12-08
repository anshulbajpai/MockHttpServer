package org.mockhttpserver

import com.sun.jersey.api.client.ClientResponse
import javax.ws.rs.core.MediaType

class MockHttpServerSpec extends BddSpec with RestSupport with JsonSupport{

  case class Entity(name : String,age : Int)

  object RequestEntity extends Entity("Request", 10)
  object ResponseEntity extends Entity("Response", 10)

  def baseUri = "http://localhost:8080/"
  describe("Mock Http Server"){

    var server : MockHttpServer = null

    it("satisfy the get expectations"){
      server = MockHttpServer("localhost",8080) {
        GET("/foo") -> Response(200, Some(PlainText("foo me")))
      }.start

      val response = jerseyClient.resource(baseUri + "foo").get(classOf[ClientResponse])
      response.getStatus should  be(200)
      response.getType should be(MediaType.TEXT_PLAIN_TYPE)
      response.getEntity(classOf[String]) should be("foo me")
    }

    it("satisfy the post expectations with Json Request"){
      server = MockHttpServer("localhost",8080) {
        POST("/foo", Some(Json(toJson(RequestEntity)))) -> Response(200, Some(Json(toJson(ResponseEntity))))
      }.start

      val response = jerseyClient.resource(baseUri + "foo").`type`("application/json").post(classOf[ClientResponse], toJson(RequestEntity))
      response.getStatus should  be(200)
      response.getType should be(MediaType.APPLICATION_JSON_TYPE)
      response.getEntity(classOf[String]) should be(toJson(ResponseEntity))
    }

    it("returns 404 if the expectations is not satisfied"){
      server = MockHttpServer("localhost",8080) {
        POST("/foo", Some(Json(toJson(RequestEntity)))) -> Response(200, Some(Json(toJson(ResponseEntity))))
        POST("/foo", Some(Json(toJson(RequestEntity)))) -> Response(200, Some(Json(toJson(ResponseEntity))))
      }.start

      val response = jerseyClient.resource(baseUri + "foo").get(classOf[ClientResponse])
      response.getStatus should  be(404)
      response.getType should be(MediaType.TEXT_PLAIN_TYPE)
      response.getEntity(classOf[String]) should be("Request was not matched GET localhost:8080/foo")
    }

    after{
      server.stop
    }
  }


}
