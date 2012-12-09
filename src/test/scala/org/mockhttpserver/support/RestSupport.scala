package org.mockhttpserver.support

import com.sun.jersey.api.client.Client

trait RestSupport {
  val jerseyClient = Client.create()
}
