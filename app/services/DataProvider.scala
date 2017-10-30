package services

import javax.inject._

import models.Country
import play.api.{Configuration, Logger}
import play.api.inject.ApplicationLifecycle
import kantan.csv._
import kantan.csv.ops._
import kantan.csv.generic._

import scala.concurrent.Future

@Singleton
class DataProviderImpl @Inject() (configuration: Configuration, appLifecycle: ApplicationLifecycle){

  val logger: Logger = Logger(this.getClass)

  def goodbye(): Unit = println("Goodbye!")

  def start = {
    logger.info("loading countries data")

    val rawData : java.net.URL = getClass.getResource(s"/${configuration.get[String]("resource_countries")}")

    val reader = rawData.readCsv[List, Country](rfc.withHeader)

    DataProviderImpl.loadCountries(reader.filter(d => d.isSuccess).map(x => x.get))
  }

  // When the application starts, register a stop hook with the
  // ApplicationLifecycle object. The code inside the stop hook will
  // be run when the application stops.
  appLifecycle.addStopHook { () =>
    goodbye()
    Future.successful(())
  }

  // Called when this singleton is constructed
  start

}

object DataProviderImpl {

  var countries : List[Country] = List[Country]();

  def loadCountries(list : List[Country]) = {
    countries = list;
  }
}
