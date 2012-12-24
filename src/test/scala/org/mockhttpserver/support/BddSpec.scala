package org.mockhttpserver.support

import org.scalatest.{BeforeAndAfter, FunSpec}
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.mock.MockitoSugar
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class JunitBddSpec extends FunSpec with ShouldMatchers with BeforeAndAfter with ResourcesSupport with  MockitoSugar
