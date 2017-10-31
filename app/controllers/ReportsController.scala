package controllers

import javax.inject._
import models.{CountryAirportResult, Country, QueryResult}
import play.api._
import play.api.libs.json.{JsValue, Json}
import play.api.mvc._
import services.DataProviderImpl
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

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
