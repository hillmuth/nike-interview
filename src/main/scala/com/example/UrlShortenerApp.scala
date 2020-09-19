package com.example

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import scala.io.StdIn

object UrlShortenerApp {

  def main(args: Array[String]): Unit = {

    implicit val system = ActorSystem(Behaviors.empty, "url-shortener-system")
    // needed for the future flatMap/onComplete in the end
    implicit val executionContext = system.executionContext

    val hashStorageService = new HashStorageService
    val hashingService = new HashingService(hashStorageService)

    val urlShortener = new UrlShortenerRoutes(hashingService, hashStorageService)(system)

    val bindingFuture = Http().newServerAt("localhost", 8080).bind(urlShortener.routes)

    println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
    StdIn.readLine() // let it run until user presses return
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done
  }
}
