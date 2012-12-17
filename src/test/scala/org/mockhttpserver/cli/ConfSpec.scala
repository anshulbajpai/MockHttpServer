package org.mockhttpserver.cli

import org.mockhttpserver.support.BddSpec

class ConfSpec extends BddSpec {

  describe("Conf") {

    it("gives the port option correctly") {
      new Conf("--port 8081 --basePath abcd").port() should be === 8081
      new Conf("-p 8081 --basePath abcd").port() should be === 8081
    }

    it("defualts the port to 8080"){
      new Conf("--basePath abcd").port() should be === 8080
    }

    it("gives the base path correctly"){
      new Conf("--basePath /home/user/foo").basePath() should be === "/home/user/foo"
      new Conf("-b /home/user/foo").basePath() should be === "/home/user/foo"
    }

    it("gives the host as localhost"){
      new Conf("--basePath abcd").host should be === "localhost"
    }
  }
}
