package models

import play.api.libs.json._

case class Country(id: Long, code: String, name: String, continent: String, wiki_link: String, keywords: Option[String])

case class Airport (id: Long, ident: String, typeOf: String, name: String, latitude_deg: Float, longitude_deg: Float,
                    elevation_ft: Long, continent: String, iso_country: String, iso_region: String, municipality: Option[String],
                    scheduled_service: Option[String], gps_code : Option[String], iata_code: Option[String], local_code: String,
                    home_link: Option[String], wikipedia_link : Option[String], keywords: Option[String])

case class Runway (id: Long, airport_ref: Long, airport_ident: String, length_ft: Long, width_ft: Long
                  ,surface: String, lighted: Int, closed: Int, le_ident: String)


case class CountryAirportResult(country: Country, airport: Seq[Airport])

case class CountryRunwayType(country: Country, runwayType : Set[String])


object Country {
  implicit val countryWrites = new Writes[Country] {
    def writes(country: Country) = Json.obj(
      "code" -> country.code,
      "nsme" -> country.name
    )
  }
}

object Airport {
  implicit val airportWrites = new Writes[Airport] {
    override def writes(o: Airport): JsValue = Json.obj(
      "ident" -> o.ident,
      "type" -> o.typeOf,
      "name" -> o.name
    )
  }
}

object Runway {
  implicit val runwayWrites = new Writes[Runway] {
    override def writes(o: Runway): JsValue = Json.obj(
      "id" -> o.id,
      "airport reference" -> o.airport_ref,
      "airport identifier" -> o.airport_ident,
      "length in ft" -> o.length_ft,
      "surface" -> o.surface
    )
  }
}

case class QueryResult(country: Country, airportRunwayList: Seq[AirportRunwayResult])

case class AirportRunwayResult(airport: Airport, runwayList: Seq[Runway])

object AirportRunwayResult {

  implicit val resultWrites = new Writes[AirportRunwayResult] {
    override def writes(o: AirportRunwayResult): JsValue = Json.obj(
      "airport_name" -> o.airport.name,
      "airport_ident" -> o.airport.ident,
      "runways" -> o.runwayList
    )
  }
}

object QueryResult {
  implicit val resultWrites = new Writes[QueryResult] {
    override def writes(o: QueryResult): JsValue = Json.obj(
      "country_name" -> o.country.name,
      "country_code" -> o.country.code,
      "airports" -> o.airportRunwayList
    )
  }

  implicit val reportWrites = new Writes[QueryResult] {
    override def writes(o: QueryResult): JsValue = Json.obj(
      "country_name" -> o.country.name,
      "country_code" -> o.country.code,
      "airports" -> o.airportRunwayList.size
    )
  }
}

object CountryAirportResult {
  implicit val resultWrites = new Writes[CountryAirportResult] {
    override def writes(o: CountryAirportResult): JsValue = Json.obj(
      "country_name" -> o.country.name,
      "country_code" -> o.country.code,
      "airports" -> o.airport.size
    )
  }
}

object CountryRunwayType {
  implicit val resultWrites = new Writes[CountryRunwayType] {
    override def writes(o: CountryRunwayType): JsValue = Json.obj(
      "country_name" -> o.country.name,
      "country_code" -> o.country.code,
      "runways" -> o.runwayType
    )
  }
}