package com.example

import java.util.concurrent.atomic.AtomicInteger

import com.example.Entities.{InputUrl, UrlMapping}

class HashingService(storageService: urlStorage) {

  private val atomicInteger = new AtomicInteger()

  def hash(): Int = {
    atomicInteger.incrementAndGet()
  }

  def hashUrl(url :String): (Int, String) = {
    val shortendUrl = hash()
    (shortendUrl, url)
  }

  def processUrl(input: InputUrl): UrlMapping = {
    val hashPair = hashUrl(input.url)
    storageService.put(hashPair._1, hashPair._2)

    UrlMapping(hashPair._2, hashPair._1)
  }
}

