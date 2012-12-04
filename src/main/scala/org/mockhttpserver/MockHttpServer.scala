package org.mockhttpserver

import org.simpleframework.transport.connect.SocketConnection
import org.simpleframework.http.core.Container
import org.simpleframework.http
import java.net.InetSocketAddress
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
    expectations.get(getRequest(req)) match {
      case Some(r) => {
        setResponse(r.status, r.contentType, r.body)
      }
      case None => {
        setResponse(404, "text/plain", "Request was not matched %s %s:%s%s".format(req.getMethod, host, port, req.getTarget))
      }
    }
    def setResponse(status: Int, contentType: String, body: Any) {
      resp.setCode(status)
      resp.set("Content-Type", contentType)
      val stream: PrintStream = resp.getPrintStream
      stream.print(body)
      stream.close()
    }
  }

  private def getRequest(req: http.Request) = req.getMethod match {
    case "GET" => GET(req.getTarget)
    case "POST" => POST(req.getTarget)
  }
}

object MockHttpServer {
  def apply(host: String, port: Int)(expectations: Map[Request, Response]) = new MockHttpServer(host, port, expectations)
}