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

    val request = extract(req)

    val response = expectations.keys.find(r => r ~= request) match {
      case Some(r) => expectations(r)
      case _ => Response(404, Some(Body("text/plain", "Request was not matched %s %s:%s%s".format(req.getMethod, host, port, req.getTarget))))
    }

    send(response)

    def send(response: Response) {
      resp.setCode(response.status)
      req.getMethod match {
        case "DELETE" =>  resp.close()
        case _ if(!response.body.isDefined) =>  resp.close()
        case _  => {
          val body = response.body.get
          resp.set("Content-Type", body.contentType)
          val stream = resp.getPrintStream
          stream.print(body.entity)
          stream.close()
        }
      }
    }
  }

  private def extract(req: http.Request) = req match {
      case GET(g) => g
      case DELETE(d) => d
      case POST(p) => p
      case PUT(p) => p
  }
}

object MockHttpServer {
  def apply(host: String, port: Int)(expectations: (Request, Response) *) = new MockHttpServer(host, port, expectations.toMap)
}



