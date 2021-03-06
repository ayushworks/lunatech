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
class QueryController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  val queryLogger: Logger = Logger(this.getClass)


  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }

  def getCountryByName(name: String) = Action.async {
    implicit request: Request[AnyContent] =>
      queryLogger.info(s"Received country by name request for param ${name}")
      Future{
        import QueryResult.resultWrites
        val result = DataProviderImpl.queryByCountryName(name)
        if(result.isDefined) {
          Ok(Json.toJson(result.get))
        }
        else {
          Ok(s"Cannot find country with name ${name}")
        }
      }
  }

  def getCountryByCode(code: String) = Action.async {
    implicit request: Request[AnyContent] =>
      queryLogger.info(s"Received country by code request for param ${code}")
      Future{
        import QueryResult.resultWrites
        val result = DataProviderImpl.queryByCountryCode(code)
        if(result.isDefined) {
          Ok(Json.toJson(result.get))
        }
        else {
          Ok(s"Cannot find country with code ${code}")
        }
      }
  }

}
