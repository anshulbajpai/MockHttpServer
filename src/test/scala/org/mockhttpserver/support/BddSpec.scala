package org.mockhttpserver.support

import org.scalatest.{BeforeAndAfter, FunSpec}
import org.scalatest.matchers.ShouldMatchers

trait BddSpec extends FunSpec with ShouldMatchers with BeforeAndAfter with ResourcesSupport
