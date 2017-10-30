package controllers

import javax.inject._

import models.{AirportRunwayResult, Country, QueryResult}
import play.api._
import play.api.libs.json.{JsValue, Json}
import play.api.mvc._
import services.DataProviderImpl

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class QueryController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  val queryLogger: Logger = Logger(this.getClass)

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }

  def getCountryByName(name: String) = Action.async {
    implicit request: Request[AnyContent] =>
      queryLogger.info(s"Received country by name request for param ${name}")
      Future{
        val countryOption = DataProviderImpl.countries.filter(c => c.name.equals(name)).headOption
        if(countryOption.isDefined) {
          val country = countryOption.get
          val airports = DataProviderImpl.airports.filter(a => a.iso_country.equals(country.code))
          val runways = airports.map {
            a => AirportRunwayResult(a, DataProviderImpl.runways.filter(r => r.airport_ref==a.id || r.airport_ident.equals(a.ident)))
          }
          Ok(Json.toJson(QueryResult(country,runways)))
        }
        else Ok(s"Cannot find country with name ${name}")
      }
  }

  def getCountryByCode(code: String) = Action.async {
    implicit request: Request[AnyContent] =>
      queryLogger.info(s"Received country by code request for param ${code}")
      Future{
        Ok(convertCountriesToJson(DataProviderImpl.countries.filter(c => c.code.equals(code))))
      }
  }

  def convertCountriesToJson(countries: Seq[Country]): JsValue = Json.toJson(countries)
}
