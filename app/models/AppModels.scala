package models

import play.api.libs.json._

case class Country (id: Long,
   code: String,
   name: String,
   continent: String,
   wiki_link: String,
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



