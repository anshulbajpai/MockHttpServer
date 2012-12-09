package org.mockhttpserver.core

import org.simpleframework.transport.connect.SocketConnection
import org.simpleframework.http.core.Container
import org.simpleframework.http
import java.net.InetSocketAddress

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

    val unmatchedResponse = Response(404, Some(Body("text/plain", "Request was not matched %s %s:%s%s".format(req.getMethod, host, port, req.getTarget))))

    setResponse(expectations.get(createRequest(req)).getOrElse(unmatchedResponse))

    def setResponse(response: Response) {
      resp.setCode(response.status)
      val body = response.body.get
      resp.set("Content-Type", body.contentType)
      val stream = resp.getPrintStream
      stream.print(body.entity)
      stream.close()
    }
  }

  private def createRequest(req: http.Request) = req.getMethod match {
    case "GET" => GET(req.getTarget)
    case "POST" => POST(req.getTarget, req.getContent match {
      case c if !c.isEmpty=> Some(Body(req.getContentType.toString, c))
      case _ => None
    })
  }
}

object MockHttpServer {
  def apply(host: String, port: Int)(expectations: (Request, Response) *) = new MockHttpServer(host, port, expectations.toMap)
}