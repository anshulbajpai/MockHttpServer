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
        GET("/foo") -> Response(200, Some(PlainText("foo me")))
      ).start

      verifyResponse(200, MediaType.TEXT_PLAIN_TYPE, "foo me"){
        jerseyClient.resource(baseUri + "foo").get(classOf[ClientResponse])
      }
    }

    it("satisfy the post expectations with Json Request"){
      server = MockHttpServer("localhost",8080) {
        POST("/foo", Some(Json(toJson(RequestEntity)))) -> Response(200, Some(Json(toJson(ResponseEntity))))
      }.start

      verifyResponse(200, MediaType.APPLICATION_JSON_TYPE, toJson(ResponseEntity)){
        jerseyClient.resource(baseUri + "foo").`type`("application/json").post(classOf[ClientResponse], toJson(RequestEntity))
      }
    }

    it("returns 404 if the expectations is not satisfied"){
      server = MockHttpServer("localhost",8080) {
        POST("/foo", Some(Json(toJson(RequestEntity)))) -> Response(200, Some(Json(toJson(ResponseEntity))))
        POST("/foo", Some(Json(toJson(RequestEntity)))) -> Response(200, Some(Json(toJson(ResponseEntity))))
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
