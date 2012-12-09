package org.mockhttpserver.resource

import org.mockhttpserver.support.BddSpec

class ExpectationsSourceReaderSpec extends BddSpec {
    describe("Source Reader"){
      it("returns the source at the path"){
        val source = new ExpectationsSourceReader().read(getClass.getResource("/expectations").toString + "/post.expectations")
        source should be(
          Some("""*/foo1->204*/foo2[{"name":"request","age":"10"}]@application/json->200*/foo3->200[{"name":"response","age":"10"}]@application/json*/foo4[{"name":"request","age":"10"}]@application/json->200[{"name":"response","age":"10"}]@application/json*/foo5[hi]@text/plain->200[bye]@text/plain""")
        )
      }

      it("does not return anything if the source is not found"){
        new ExpectationsSourceReader().read(getClass.getResource("/expectations").toString + "/post123.expectations") should be(None)
      }
    }
}
