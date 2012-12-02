package org.mockhttpserver

import org.apache.http.HttpStatus
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.commons.io.IOUtils

class MockHttpServerSpec extends  BddSpec{

  describe("Mock Http Server"){
    var server : MockHttpServer = null
    it("satisfy the get expectations"){
      server = MockHttpServer("localhost",8080) {Map(
        GET("/foo") -> Response(HttpStatus.SC_OK, "text/plain"){"foo me"}
      )}.start

      val response = new DefaultHttpClient().execute(new HttpGet("http://localhost:8080/foo"))
      val responseBody = IOUtils.toString(response.getEntity.getContent)
      response.getStatusLine.getStatusCode should be(200)
      responseBody should be("foo me")
    }

    it("return 404 if the expectations is not satisfied"){
      server = MockHttpServer("localhost",8080) {Map(
        GET("/foo") -> Response(HttpStatus.SC_OK, "text/plain"){"foo me"},
        GET("/foo/bar") -> Response(HttpStatus.SC_OK, "text/plain"){"foo me"}
      )}.start

      val response = new DefaultHttpClient().execute(new HttpGet("http://localhost:8080/bar"))
      val responseBody = IOUtils.toString(response.getEntity.getContent)
      response.getStatusLine.getStatusCode should be(404)
      responseBody should be("Request was not matched GET localhost:8080/bar")
    }

    after{
      server.stop
    }
  }

}
