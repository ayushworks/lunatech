package models

import play.api.libs.json._

case class QueryResult(result: (Country, List[Airport]))


object QueryResult {
  implicit object QueryResultFormat extends Format[QueryResult] {
    override def reads(json: JsValue): JsResult[QueryResult] = ???

    override def writes(o: QueryResult): JsValue = {
      val jsResult = Seq(
        "country" -> JsObject(o.result._1),
        "airports" -> JsArray(o.result._2),
      )
      JsObject(jsResult)
    }
  }
}
