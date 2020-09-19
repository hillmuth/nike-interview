package com.example

import org.scalatest.flatspec.AnyFlatSpec

class HashStorageServiceTest extends AnyFlatSpec {

  val storageService = new HashStorageService

  val key = 123
  val url = "http://foo.bar"

  "insert" should "add value to map" in {
    storageService.put(key, url)
    assert(storageService.get(123) === Some(url) )
  }

  "value not stored" should "return None" in {
    storageService.put(key, url)
    assert(storageService.get(456) === None)
  }

}
