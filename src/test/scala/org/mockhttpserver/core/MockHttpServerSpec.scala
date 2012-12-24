package org.mockhttpserver.core

import com.sun.jersey.api.client.ClientResponse
import javax.ws.rs.core.MediaType
import scala.Some
import org.mockhttpserver.support.{RestSupport, JsonSupport, BddSpec}
import org.mockhttpserver.{Json, PlainText}

class MockHttpServerSpec extends BddSpec with RestSupport with JsonSupport{

  case class Entity(name : String,age : Int)

  object RequestEntity extends Entity("Request", 10)
  object ResponseEntity extends Entity("Response", 10)

  describe("Mock Http Server"){
    def baseUri = "http://localhost:8080/"

    var server : MockHttpServer = null


    it("satisfy the get expectations"){

      server = MockHttpServer("localhost",8080)(
        Get("/foo") -> Response(200, Some(PlainText("foo get"))),
        Post("/foo", None) -> Response(200, Some(PlainText("foo post")))
      ).start

      verifyResponse(200, MediaType.TEXT_PLAIN_TYPE, "foo get"){
        jerseyClient.resource(baseUri + "foo").get(classOf[ClientResponse])
      }
    }

//    it("satisfy expectations of url with regex pattern"){
//
//      server = MockHttpServer("localhost",8080)(
//        Get("/foo.*") -> Response(200, Some(PlainText("foo get")))
//      ).start
//
//      verifyResponse(200, MediaType.TEXT_PLAIN_TYPE, "foo get"){
//        jerseyClient.resource(baseUri + "foo").get(classOf[ClientResponse])
//      }
//      verifyResponse(200, MediaType.TEXT_PLAIN_TYPE, "foo get"){
//        jerseyClient.resource(baseUri + "foo1").get(classOf[ClientResponse])
//      }
//      verifyResponse(200, MediaType.TEXT_PLAIN_TYPE, "foo get"){
//        jerseyClient.resource(baseUri + "foo1/foo2").get(classOf[ClientResponse])
//      }
//    }



    it("satisfy the delete expectations"){

      server = MockHttpServer("localhost",8080)(
        Delete("/foo") -> Response(204)
      ).start

      val response = jerseyClient.resource(baseUri + "foo").delete(classOf[ClientResponse])
      response.getStatus should be === 204
    }

    it("satisfy the post expectations with Json Request"){
      server = MockHttpServer("localhost",8080) {
        Post("/foo", Some(Json(RequestEntity))) -> Response(200, Some(Json(ResponseEntity)))
      }.start

      verifyResponse(200, MediaType.APPLICATION_JSON_TYPE, toJson(ResponseEntity)){
        jerseyClient.resource(baseUri + "foo").`type`("application/json").post(classOf[ClientResponse], toJson(RequestEntity))
      }
    }

    it("satisfy the put expectations with Json Request"){
      server = MockHttpServer("localhost",8080) {
        Put("/foo", Some(Json(RequestEntity))) -> Response(200, Some(Json(ResponseEntity)))
      }.start

      verifyResponse(200, MediaType.APPLICATION_JSON_TYPE, toJson(ResponseEntity)){
        jerseyClient.resource(baseUri + "foo").`type`("application/json").put(classOf[ClientResponse], toJson(RequestEntity))
      }
    }

    it("returns 404 if the expectations is not satisfied"){
      server = MockHttpServer("localhost",8080) {
        Post("/foo", Some(Json(RequestEntity))) -> Response(200, Some(Json(ResponseEntity)))
        Post("/foo", Some(Json(RequestEntity))) -> Response(200, Some(Json(ResponseEntity)))
      }.start

      verifyResponse(404, MediaType.TEXT_PLAIN_TYPE, "Request was not matched GET localhost:8080/foo"){
        jerseyClient.resource(baseUri + "foo").get(classOf[ClientResponse])
      }
    }

    def verifyResponse(status : Int, `type` : MediaType, entity : String)(response: ClientResponse) {
      response.getStatus should be(status)
      response.getType should be(`type`)
      response.getEntity(classOf[String]) should be(entity)
    }

    after{
      server.stop
    }
  }
}
