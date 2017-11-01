package controllers

import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.test.Helpers._
import play.api.test._

class QueryControllerSpec extends PlaySpec with GuiceOneAppPerSuite with Injecting {

  "QueryController country by name GET" should {

    "render the country details" in {
      val controller = new QueryController(stubControllerComponents())
      val country = controller.getCountryByName("Andorra").apply(FakeRequest(GET, "/country/name/Andorra"))

      status(country) mustBe OK
      contentType(country) mustBe Some("application/json")
      val expected = s"""{"country_name":"Andorra","country_code":"AD","airports":[{"airport_name":"Andorra la Vella Heliport","airport_ident":"AD-ALV","runways":[]}]}"""
      contentAsString(country) must include (expected)

    }

  }

  "QueryController country by code GET" should {

    "render the country details" in {
      val controller = new QueryController(stubControllerComponents())
      val country = controller.getCountryByCode("AD").apply(FakeRequest(GET, "/country/code/AD"))

      status(country) mustBe OK
      contentType(country) mustBe Some("application/json")
      val expected = s"""{"country_name":"Andorra","country_code":"AD","airports":[{"airport_name":"Andorra la Vella Heliport","airport_ident":"AD-ALV","runways":[]}]}"""
      contentAsString(country) must include (expected)

    }

  }

  "QueryController country by code GET for invalid code" should {

    "render error message" in {
      val controller = new QueryController(stubControllerComponents())
      val country = controller.getCountryByCode("SDSDS").apply(FakeRequest(GET, "/country/code/SDSDS"))

      status(country) mustBe OK
      contentType(country) mustBe Some("text/plain")
      contentAsString(country) must include ("Cannot find country with code")

    }

  }
}
