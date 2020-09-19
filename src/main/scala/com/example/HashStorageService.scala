package com.example

import scala.collection.mutable

trait urlStorage {
  def put(key: Int, value: String)
  def get(Key: Int): Option[String]
}

class HashStorageService extends urlStorage {

  private val hashStorage: mutable.Map[Int, String] = mutable.Map.empty

  def put(key: Int, value: String): Unit = {
     hashStorage.addOne((key,value))
  }

  def get(key: Int): Option[String] = {
    hashStorage.get(key)
  }
}
