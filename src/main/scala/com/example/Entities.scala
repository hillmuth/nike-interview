package com.example

import spray.json.DefaultJsonProtocol.{StringJsonFormat, IntJsonFormat, jsonFormat1, jsonFormat2}

object Entities {

  case class UrlMapping(originalUrl: String, path: Int)

  implicit val urlMappingJsonFormat = jsonFormat2(UrlMapping)

  case class InputUrl(url: String)
  implicit val urlJsonFormat = jsonFormat1(InputUrl)

}



