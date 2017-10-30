package models

import play.api.libs.json._

case class Country(id: Long, code: String, name: String, continent: String, wiki_link: String, keywords: Option[String])

case class Airport (id: Long, ident: String, typeOf: String, name: String, latitude_deg: Float, longitude_deg: Float,
                    elevation_ft: Long, continent: String, iso_country: String, iso_region: String, municipality: Option[String],
                    scheduled_service: Option[String], gps_code : Option[String], iata_code: Option[String], local_code: String,
                    home_link: Option[String], wikipedia_link : Option[String], keywords: Option[String])

case class Runway (id: Long, airport_ref: Long, airport_ident: String)

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
      "airport_ref" -> o.airport_ref,
      "airport_ident" -> o.airport_ident
    )
  }
}

case class QueryResult(a: Country, b: Seq[AirportRunwayResult])

case class AirportRunwayResult(a: Airport, b: Seq[Runway])

object AirportRunwayResult {

  implicit val resultWrites = new Writes[AirportRunwayResult] {
    override def writes(o: AirportRunwayResult): JsValue = Json.obj(
      "airport_name" -> o.a.name,
      "airport_ident" -> o.a.ident,
      "runways" -> o.b
    )
  }
}

object QueryResult {
  implicit val resultWrites = new Writes[QueryResult] {
    override def writes(o: QueryResult): JsValue = Json.obj(
      "country_name" -> o.a.name,
      "country_code" -> o.a.code,
      "airports" -> o.b
    )
  }
}