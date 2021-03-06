package jp.co.bizreach.play2handlebars

import org.scalatest.{BeforeAndAfter, FunSpec}

class HandlebarsPluginSpec extends FunSpec with FakePlayHelper with BeforeAndAfter {

  describe("Handlebars plugin") {
    describe("when the plugin generates template") {
      it("should generate a simple view") {
        runApp(PlayApp()) {
          assert(HBS.engine.templates.keys.size === 0)
        }
      }
    }

    describe("when the cache is enabled") {
      it("should cache templates") {
        runApp(PlayApp("play2handlebars.enableCache" -> "true")) {
          // Because HBS is just an object, it should be cleared to do test multiple times
          HBS.engine.templates.clear()

          // Quick workaround for the indeterministic result. "assert()"
          val res0 = HBS.engine.templates.keys.size === 0
          val res1 = HBS("test-template1", "who" -> "World").toString === "Hello World!"
          val res2 = HBS.engine.templates.keys.size === 1
          val res3 = HBS("test-template1", "who" -> "Play").toString === "Hello Play!"
          val res4 = HBS.engine.templates.keys.size === 1

          assert(res0)
          assert(res1)
          assert(res2)
          assert(res3)
          assert(res4)
        }
      }
    }

    describe("when the cache is NOT enabled") {
      it("should NOT cache templates") {
        runApp(PlayApp()) {
          assert(HBS.engine.templates.keys.size === 0)
          assert(HBS("test-template1", "who" -> "World").toString === "Hello World!")
          assert(HBS.engine.templates.keys.size === 0)
          assert(HBS("test-template1", "who" -> "Play").toString === "Hello Play!")
          assert(HBS.engine.templates.keys.size === 0)
        }
      }
    }
  }
}
