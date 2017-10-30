package services

import javax.inject._

import models.{Airport, Country}

import scala.concurrent.Future

@Singleton
class DataProviderImpl @Inject() (configuration: Configuration, appLifecycle: ApplicationLifecycle){

  val logger: Logger = Logger(this.getClass)

  def goodbye(): Unit = println("Goodbye!")

  def loadCountries = {
    logger.info("loading countries data")

    val rawData : java.net.URL = getClass.getResource(s"/${configuration.get[String]("resource_countries")}")

    val reader = rawData.readCsv[List, Country](rfc.withHeader)

    DataProviderImpl.loadCountries(reader.filter(d => d.isSuccess).map(x => x.get))
  }

  def loadAirports = {
    logger.info("loading airports data")

    val rawData : java.net.URL = getClass.getResource(s"/${configuration.get[String]("resource_airports")}")

    val reader = rawData.readCsv[List, Airport](rfc.withHeader)

    DataProviderImpl.loadAirports(reader.filter(d => d.isSuccess).map(x => x.get))
  }

  // When the application starts, register a stop hook with the
  // ApplicationLifecycle object. The code inside the stop hook will
  // be run when the application stops.
  appLifecycle.addStopHook { () =>
    goodbye()
    Future.successful(())
  }

  // Called when this singleton is constructed
  loadCountries
  loadAirports
}

object DataProviderImpl {

  var countries : List[Country] = List[Country]();
  var airports : List[Airport] = List[Airport]();

  def loadCountries(list : List[Country]) = {
    countries = list;
  }

  def loadAirports(list : List[Airport]) = {
    airports = list;
  }
}
