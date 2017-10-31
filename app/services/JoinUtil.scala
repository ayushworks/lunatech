package services

import models.{AirportRunwayResult, Country, CountryAirportResult, QueryResult}

/**
  * Created by AyushM on 10/31/2017.
  */
object JoinUtil {

  def joinCountryAirportRunways(country: Country) : QueryResult = {
    val airports = DataProviderImpl.airports.filter(a => a.iso_country.equals(country.code))
    val runways = airports.map {
      a => AirportRunwayResult(a, DataProviderImpl.runways.filter(r => r.airport_ref==a.id || r.airport_ident.equals(a.ident)))
    }
    (QueryResult(country,runways))
  }

  def joinCountryAirport(country: Country) : CountryAirportResult = {
    val airports = DataProviderImpl.airports.filter(a => a.iso_country.equals(country.code))
    CountryAirportResult(country, airports)
  }
}
