package org.mockhttpserver

import org.simpleframework.transport.connect.SocketConnection
import org.simpleframework.http.core.Container
import org.simpleframework.http
import java.net.InetSocketAddress
import org.apache.http.entity.ContentType
import java.io.PrintStream

class MockHttpServer(host: String, port: Int, expectations: Map[Request, Response]) extends Container {

  private val socketConnection: SocketConnection = new SocketConnection(this)

  def start = {
    socketConnection.connect(new InetSocketAddress(host, port))
    this
  }

  def stop {
    socketConnection.close()
  }

  def handle(req: http.Request, resp: http.Response) {
    expectations.get(Request(req.getPath.toString)) match{
      case Some(r) => {
        resp.setCode(r.status)
        resp.set("Content-Type", r.contentType)
        val stream: PrintStream = resp.getPrintStream
        stream.print(r.body)
        stream.close()
      }
      case None => throw new RuntimeException("Request wasn't expected %s".format(req))
    }
  }
}

object MockHttpServer {
  def apply(host: String, port: Int)(expectations: Map[Request, Response]) = new MockHttpServer(host, port, expectations)
}