import org.mockhttpserver.core.{Response, GET, MockHttpServer}
import org.mockhttpserver.{Json, PlainText}

MockHttpServer("localhost",8080)(
  GET("/foo") -> Response(200, Some(PlainText("foo me"))),
  GET("/foo1/") -> Response(200, Some(Json("""{"name":"foo"}""")))
).start

println("Started")