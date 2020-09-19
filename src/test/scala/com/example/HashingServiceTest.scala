package com.example

import com.example.Entities.{InputUrl, UrlMapping}
import org.scalatest.flatspec.AnyFlatSpec

class HashingServiceTest extends AnyFlatSpec {

  val hashStorageService = new HashStorageService
  val unit = new HashingService(hashStorageService)

  "hash url" should "return values in the UrlMapping case class" in {
    val u = InputUrl("http://www.foo.bar")

    assert(unit.processUrl(u) ===  UrlMapping( "http://www.foo.bar", 1))
  }

  "in memory hash simulator" should "increment" in {
    val samples = Seq("one", "two", "three")
    val ints: Seq[Int] = samples.map( h =>  unit.hash() )
    ints.length === 3
    ints.last === 4
  }
}
