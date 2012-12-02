package org.mockhttpserver

import org.simpleframework.transport.connect.SocketConnection
import org.simpleframework.http.core.Container
import org.simpleframework.http
import java.net.InetSocketAddress
import org.apache.http.entity.ContentType
import java.io.PrintStream

class MockHttpServer(host: String, port: Int, expectations: List[Pair[Request, Response]]) extends Container {

  private val socketConnection: SocketConnection = new SocketConnection(this)

  def start = {
    socketConnection.connect(new InetSocketAddress(host, port))
    this
  }

  def stop {
    socketConnection.close()
  }

  def handle(req: http.Request, resp: http.Response) {

    resp.setCode(200)
    resp.set("Content-Type", ContentType.APPLICATION_JSON.toString)
    val stream: PrintStream = resp.getPrintStream
    stream.print("foo me")
    stream.close()
  }
}

object MockHttpServer {
  def apply(host: String, port: Int)(expectations: Pair[Request, Response]*) = new MockHttpServer(host, port, expectations.toList)
}