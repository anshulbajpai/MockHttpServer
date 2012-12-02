package org.mockhttpserver

import org.apache.http.HttpStatus
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.commons.io.IOUtils

class MockHttpServerSpec extends  BddSpec{

  describe("Mock Http Server"){

    it("should add the get expectations"){
      val server = MockHttpServer("localhost",8080) {Map(
        GET("/foo") -> Response(HttpStatus.SC_OK, "text/plain"){"foo me"},
        GET("/foo/bar") -> Response(HttpStatus.SC_OK, "text/plain"){"bar"}
      )}.start

      val response = new DefaultHttpClient().execute(new HttpGet("http://localhost:8080/foo"))
      val responseBody = IOUtils.toString(response.getEntity.getContent)
      responseBody should be("foo me")
      response.getStatusLine.getStatusCode should be(200)
      server.stop
    }
  }

}
