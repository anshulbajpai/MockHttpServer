package org.mockhttpserver.expectations

import org.mockhttpserver.support.BddSpec
import org.mockhttpserver.core._
import org.mockhttpserver.core.Put
import org.mockhttpserver.core.Body
import org.mockhttpserver.core.Get
import scala.Some
import org.mockhttpserver.core.Post

class TransformSpec extends BddSpec {

  describe("Transform"){

    val body = Some(mock[Body])

    it("transform to get"){
      Transform.GetTransform.execute("url") should be === Get("url")
      Transform.GetTransform.execute("url",body)  should be === Get("url")
    }

    it("transform to delete"){
      Transform.DeleteTransform.execute("url") should be === Delete("url")
      Transform.DeleteTransform.execute("url",body)  should be === Delete("url")
    }

    it("transforms to post"){
      Transform.PostTransform.execute("url") should be === Post("url")
      Transform.PostTransform.execute("url", body) should be === Post("url", body)
    }

    it("transforms to put"){
      Transform.PutTransform.execute("url") should be === Put("url")
      Transform.PutTransform.execute("url", body) should be === Put("url", body)
    }
  }

}
