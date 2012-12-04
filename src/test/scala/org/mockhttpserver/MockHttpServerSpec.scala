package org.mockhttpserver

import org.apache.http.HttpStatus
import org.apache.http.client.methods.{HttpPost, HttpGet}
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.commons.io.IOUtils
import com.sun.jersey.api.client.ClientResponse

class MockHttpServerSpec extends  BddSpec with  RestSupport{

  protected def baseUriAsString = "http://localhost:8080/"
  describe("Mock Http Server"){

    var server : MockHttpServer = null

    it("satisfy the get expectations"){
      server = MockHttpServer("localhost",8080) {Map(
        GET("/foo") -> Response(200, "text/plain"){"foo me"}
      )}.start

      rest {
        implicit s =>
          val response = "foo".GET[ClientResponse]
          response.getStatus should be(200)
          response.getHeaders.getFirst("Content-Type") should be("text/plain")
          response.toEntity[String] should be("foo me")
      }
    }

    it("satisfy the post expectations"){
      server = MockHttpServer("localhost",8080) {Map(
        GET("/foo") -> Response(200, "text/plain"){"foo get"},
        POST("/foo") -> Response(200, "text/plain"){"foo post"}
      )}.start

      rest {
        implicit s =>
          val response = "foo".POST[ClientResponse] <= ()
          response.getStatus should be(200)
          response.getHeaders.getFirst("Content-Type") should be("text/plain")
          response.toEntity[String] should be("foo post")
      }

      val response = new DefaultHttpClient().execute(new HttpPost("http://localhost:8080/foo"))
      val responseBody = IOUtils.toString(response.getEntity.getContent)
      response.getStatusLine.getStatusCode should be(200)
      responseBody should be("foo post")
    }

    it("returns 404 if the expectations is not satisfied"){
      server = MockHttpServer("localhost",8080) {Map(
        GET("/foo") -> Response(200, "text/plain"){"foo me"},
        GET("/foo/bar") -> Response(200, "text/plain"){"foo me"}
      )}.start

      rest {
        implicit s =>
          val response = "bar".POST[ClientResponse] <= ()
          response.getStatus should be(404)
          response.getHeaders.getFirst("Content-Type") should be("text/plain")
          response.toEntity[String] should be("Request was not matched POST localhost:8080/bar")
      }
    }

    after{
      server.stop
    }
  }


}
