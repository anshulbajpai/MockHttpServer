package org.mockhttpserver.support

import org.scalatest.{BeforeAndAfter, FunSpec}
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.mock.MockitoSugar

trait BddSpec extends FunSpec with ShouldMatchers with BeforeAndAfter with ResourcesSupport with  MockitoSugar
