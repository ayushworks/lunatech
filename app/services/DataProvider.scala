package services

import javax.inject._
import models.{Airport, Country, Runway, AirportRunwayResult, CountryAirportResult, CountryRunwayType}
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

    val list = reader.filter(d => d.isSuccess).map{
      x => x.get
    }

    logger.info(s"loaded ${list.size} airports")

    DataProviderImpl.loadAirports(list)
  }

  def loadRunways = {
    logger.info("loading runways data")

    val rawData : java.net.URL = getClass.getResource(s"/${configuration.get[String]("resource_runways")}")

    val reader = rawData.readCsv[List, Runway](rfc.withHeader)

    val list = reader.filter(d => d.isSuccess).map(x => x.get)

    logger.info(s"loaded ${list.size} runways")

    DataProviderImpl.loadRunways(list)

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
  loadRunways

}

object DataProviderImpl {

  var countries : List[Country] = List[Country]();
  var airports : List[Airport] = List[Airport]();
  var runways : List[Runway] = List[Runway]();
  lazy val topCountryByAirports  = topCountryAirports
  lazy val countryByRunways = countryByRunwayTypes
  var countryAirPortMap : Map[String, Airport] = Map[String,Airport]()


  def loadCountries(list : List[Country]) = {
    countries = list;
  }

  def loadAirports(list : List[Airport]) = {
    airports = list;
  }

  def loadRunways(list : List[Runway]) = {
    runways = list;
  }

  private def topCountryAirports : List[CountryAirportResult] = {
      val countryAirportResultList = countries.map {
        c => JoinUtil.joinCountryAirport(c)
      }
      countryAirportResultList.sortBy(x => x.airport.size).takeRight(10)
  }

  private def countryByRunwayTypes : List[CountryRunwayType] = {
    val countryAirportResultList = countries.map {
      country => JoinUtil.joinCountryAirportRunways(country)
    }

    countryAirportResultList.map {
      val surfaces =  scala.collection.mutable.Set[String]()
      country =>
        country.airportRunwayList.map {
        x => x.runwayList.map {
          y => surfaces.add(y.surface)
        }
      }
      CountryRunwayType(country.country, surfaces.toSet)
    }
  }
}


