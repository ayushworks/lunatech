package controllers

import javax.inject._

import models.Country
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
        Ok(convertCountriesToJson(DataProviderImpl.countries.filter(c => c.name.equals(name))))
      }
  }

  def getCountryById(id: Long) = Action.async {
    implicit request: Request[AnyContent] =>
      queryLogger.info(s"Received country by id request for param ${id}")
      Future{
           Ok(convertCountryToJson(DataProviderImpl.countries.filter(c => c.id == id).headOption.getgit))
      }
  }

  def convertCountriesToJson(countries: Seq[Country]): JsValue = Json.toJson(countries)

  def convertCountryToJson(country: Country): JsValue = Json.toJson(country)
}
