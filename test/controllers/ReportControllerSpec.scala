package controllers

import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.test.Helpers._
import play.api.test._

class ReportControllerSpec extends PlaySpec with GuiceOneAppPerSuite with Injecting {

  "ReportController country with most/min airports  GET" should {

    "render the country details by airports" in {
      val controller = new ReportsController(stubControllerComponents())
      val report = controller.report().apply(FakeRequest(GET, "/country/airports"))

      status(report) mustBe OK
      contentType(report) mustBe Some("application/json")
      contentAsString(report) must include (s""""country_code":"US","airports":42612""")
      contentAsString(report) must include (s""""country_code":"BL","airports":2""")


    }

  }

  "ReportController country by runways GET" should {

    "render the country details by runways" in {
      val controller = new ReportsController(stubControllerComponents())
      val report = controller.report1().apply(FakeRequest(GET, "/country/runways"))

      status(report) mustBe OK
      contentType(report) mustBe Some("application/json")
    }
  }

}
