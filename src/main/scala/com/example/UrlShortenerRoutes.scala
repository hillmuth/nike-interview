package com.example

import akka.actor.typed.ActorSystem
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives.{as, complete, concat, entity, get, logRequest, path, pathPrefix, post, redirect}
import akka.http.scaladsl.server.Directives._
import com.example.Entities.InputUrl
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.util.Timeout

class UrlShortenerRoutes(hashingService: HashingService, hashStorageService: HashStorageService)(implicit val system: ActorSystem[_]) {

  private implicit val timeout = Timeout.create(system.settings.config.getDuration("my-app.routes.ask-timeout"))

  val routes =  {
    concat(
      pathPrefix("redirect" / IntNumber) {
          key => hashStorageService.get(key) match {
            case Some(url) => redirect(url, StatusCodes.MovedPermanently)
            case None => complete(StatusCodes.NoContent)
          }
      },
      path("shorten") {
        post {
          logRequest("shortener")
          entity(as[InputUrl]) { url =>
            val proc: Entities.UrlMapping = hashingService.processUrl(url)
            complete(s"http://localhost:8080/redirect/${proc.path}")
          }
        }
      }
    )
  }
}
