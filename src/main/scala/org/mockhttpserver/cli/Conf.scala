package org.mockhttpserver.cli

import org.rogach.scallop.ScallopConf

class Conf(args : Array[String]) extends ScallopConf(args){

    def this(args : String) = this(args.split(" "))

    version("MockHttpServer version 1.0.0")
    banner(
      """
        |Usage: mock-http-server [OPTION...]
        |Options:
      """.stripMargin)
    footer("For rest, consult the documentation or the developer.")

    val port = opt[Int]("port", default = Some(8080), descr = "port to start the server")
    val basePath = opt[String]("basePath", required = true, descr = "base path to expectations root folder")
    val host = "localhost"
}
