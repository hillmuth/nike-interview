package com.example

import akka.actor.testkit.typed.scaladsl.ActorTestKit
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.util.ByteString
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class UrlShortenerAppTest extends AnyWordSpec with Matchers with ScalatestRouteTest {

  lazy val testKit = ActorTestKit()
  override def createActorSystem(): akka.actor.ActorSystem =
      testKit.system.classicSystem

  val hashStorageService = new HashStorageService
  val hashingService = new HashingService(hashStorageService)

  val routes = new UrlShortenerRoutes(hashingService, hashStorageService)(testKit.system).routes

  "The Url Shortener App" when {
    "receiving a URL payload" should {
      "shorten path produces a shortened url" in {

        val jsonRequest = ByteString(
          s"""
             | {"url":"http://foo.bar"}
             |""".stripMargin
        )

        val postRequest = HttpRequest(
          HttpMethods.POST,
          uri = "/shorten",
          entity = HttpEntity(MediaTypes.`application/json`, jsonRequest)
        )

        postRequest ~> routes ~> check {
          status.isSuccess() shouldEqual true
          entityAs[String] should === ("http://localhost:8080/redirect/1")
        }
    }

    "when using a shortened url" should {
      "be redirected to the original submitted" in {

          val key = 314
          hashStorageService.put(key, "http://foo.bar")

          Get("/redirect/314") ~> routes ~> check {
            status shouldEqual StatusCodes.MovedPermanently
            responseAs[String] shouldEqual "This and all future requests should be directed to <a href=\"http://foo.bar\">this URI</a>."
          }
        }
      }

     "unconfigured URLs" should {
       "return a No Content status" in {
         Get("/redirect/42") ~> routes ~> check {
           status shouldEqual StatusCodes.NoContent
         }
       }
     }
    }
  }
}
