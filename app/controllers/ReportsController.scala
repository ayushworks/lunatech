package controllers

import javax.inject._
import models.{CountryAirportResult, Country, QueryResult}
import play.api._
import play.api.libs.json.{JsValue, Json}
import play.api.mvc._
import services.DataProviderImpl
import services.JoinUtil
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class ReportsController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  val reportsLogger: Logger = Logger(this.getClass)

  def report() = Action.async {
    implicit request: Request[AnyContent] =>
      reportsLogger.info(s"Received generate report request")
      Future{
        Ok(Json.toJson(DataProviderImpl.topCountryByAirports))
      }
  }

  def report1() = Action.async {
    implicit request: Request[AnyContent] =>
      reportsLogger.info(s"Received generate report request 2")
      Future{
        Ok(Json.toJson(DataProviderImpl.countryByRunways))
      }
  }

  def convertCountriesToJson(countries: Seq[Country]): JsValue = Json.toJson(countries)
}
