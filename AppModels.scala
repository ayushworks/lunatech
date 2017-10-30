package models

import play.api.libs.json._

case class Country (id: Long,
                    code: String,
                    name: String,
                    continent: String,
                    wiki_link: String,
                    keywords: Option[String]
                   )

case class Airport (id: Long,
                    ident: String,
                    typeOf: String,
                    name: String,
                    latitude_deg: Float,
                    longitude_deg: Float,
                    elevation_ft: Long,
                    continent: String,
                    iso_country: String,
                    iso_region: String,
                    municipality: String,
                    scheduled_service: String,
                    gps_code : String,
                    iata_code: Option[String],
                    local_code: String,
                    home_link: Option[String],
                    wikipedia_link : String,
                    keywords: Option[String]
                   )

object Country {

  implicit object CountryFormat extends Format[Country] {
    override def reads(json: JsValue): JsResult[Country] = ???

    override def writes(o: Country): JsValue = {
      val jsResult = Seq(
        "code" -> JsString(o.code),
        "name" -> JsString(o.name),
        "continent" -> JsString(o.continent),
        "wiki_link" -> JsString(o.wiki_link)
      )

      if(o.keywords.isEmpty) JsObject(jsResult)
      else JsObject(jsResult :+ ("keywords" -> JsString(o.keywords.get)))
    }
  }
}

object Airport {

  implicit object AirportFormat extends Format[Airport] {
    override def reads(json: JsValue): JsResult[Airport] = ???

    override def writes(o: Airport): JsValue = {
      val jsResult = Seq(
        "ident" -> JsString(o.ident),
        "name" -> JsString(o.name),
        "country" -> JsString(o.iso_country),
        "wiki_link" -> JsString(o.wikipedia_link)
      )
      JsObject(jsResult)
    }
  }
}


