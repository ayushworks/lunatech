package services

import javax.inject._

import models._
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

    reader.filter(d => d.isSuccess).map {
      x =>
        val country = x.get
        DataProviderImpl.countryByCode(country.code) = country
        DataProviderImpl.countryByName(country.name) = country
    }
  }

  def loadAirports = {
    logger.info("loading airports data")

    val rawData : java.net.URL = getClass.getResource(s"/${configuration.get[String]("resource_airports")}")

    val reader = rawData.readCsv[List, Airport](rfc.withHeader)

    reader.filter(d => d.isSuccess).map{
      x =>
        val airport = x.get
        val current = DataProviderImpl.airportsByCountry.get(airport.iso_country)
        if(current.isDefined) {
          DataProviderImpl.airportsByCountry(airport.iso_country) = airport :: current.get
        }
        else {
          DataProviderImpl.airportsByCountry(airport.iso_country) = List[Airport](airport)
        }
    }

  }

  def loadRunways = {
    logger.info("loading runways data")

    val rawData : java.net.URL = getClass.getResource(s"/${configuration.get[String]("resource_runways")}")

    val reader = rawData.readCsv[List, Runway](rfc.withHeader)

    reader.filter(d => d.isSuccess).map{
      x =>
        val runway = x.get
        val current = DataProviderImpl.runwayByAirports.get(runway.airport_ident)
        if(current.isDefined) {
          DataProviderImpl.runwayByAirports(runway.airport_ident) = runway :: current.get
        }
        else {
          DataProviderImpl.runwayByAirports(runway.airport_ident) = List[Runway](runway)
        }
    }
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

  lazy val topCountryByAirports  = countryByAirportsSize
  lazy val countryByRunways = countryByRunwayTypes


  var countryByCode = scala.collection.mutable.Map[String, Country]()
  var countryByName = scala.collection.mutable.Map[String, Country]()

  //key is country code, iso_code of airports
  var airportsByCountry = scala.collection.mutable.Map[String, List[Airport]]()

  //key is ident Airport, ident_ref of Runway
  var runwayByAirports = scala.collection.mutable.Map[String, List[Runway]]()

  def queryByCountryName(name: String) : Option[QueryResult] = {
    var countryOption = countryByName.get(name)
    //fuzzy search
    if(!countryOption.isDefined) {

      val keyOption = countryByName.keys.filter(key => key.toLowerCase.startsWith(name.toLowerCase) || key.toLowerCase.endsWith(name.toLowerCase)).headOption
      if(keyOption.isDefined){
        countryOption = countryByName.get(keyOption.get)
      }
    }
    countryOption.map{
      country =>
        val airportsList = airportsByCountry.getOrElse(country.code, List[Airport]())
        val airportRunwayList = airportsList.map {
              airport => AirportRunwayResult(airport, runwayByAirports.getOrElse(airport.ident, List[Runway]()))
        }
        QueryResult(country, airportRunwayList)
    }
  }


  def queryByCountryCode(code: String) : Option[QueryResult] = {
    val countryOption = countryByCode.get(code)
    countryOption.map{
      country =>
        val airportsList = airportsByCountry.getOrElse(country.code, List[Airport]())
        val airportRunwayList = airportsList.map {
          airport => AirportRunwayResult(airport, runwayByAirports.getOrElse(airport.ident, List[Runway]()))
        }
        QueryResult(country, airportRunwayList)
    }
  }

  private def countryByAirportsSize : List[CountryAirportResult] = {
    val sortResult = airportsByCountry.toSeq.sortBy(_._2.size)
    val countryAirportResultList = sortResult.map {
      x =>
        CountryAirportResult(countryByCode(x._1),x._2)
    }
    countryAirportResultList.toList.takeRight(10) ::: countryAirportResultList.toList.take(10);
  }

  private def countryByRunwayTypes : List[CountryRunwayType] = {
    countryByCode.map {
      val surfaces =  scala.collection.mutable.Set[String]()
      x =>
        airportsByCountry.getOrElse(x._1, List[Airport]()).map {
          y =>
            runwayByAirports.getOrElse(y.ident, List[Runway]()).map {
              z =>
                surfaces.add(z.surface)
            }
        }
        CountryRunwayType(x._2, surfaces.toSet)
    }.toList
  }

}


