package org.mockhttpserver

import org.sjersey.client.{Rest, SimpleWebResourceProvider}

trait RestSupport extends Rest with SimpleWebResourceProvider
