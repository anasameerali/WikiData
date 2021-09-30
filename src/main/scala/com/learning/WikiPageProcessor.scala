package com.learning

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._
import org.apache.spark.sql.Encoders

case class Text(_VALUE:String,
                _bytes:Long,
                _xml_space: String){}
case class Contributor(
                        _VALUE:String,
                        _deleted:String,
                        id:String,
                        ip :String,
                        username:String
                      ){}
case  class Revision(
                      comment:String,
                      contributor: Contributor,
                      format:String,
                      id:Long,
                      minor:String,
                      model:String,
                      parentid:Long,
                      sha1:String,
                      text: Text,
                      timestamp:String
                    ){}


case class WikiData(id: Long,
                    ns:Long,
                    revision: Revision,
                    title: String
                   ){}
object  WikiPageProcessor {
  def main(args: Array[String]): Unit = {

  val sparkSession = SparkSession.builder().getOrCreate()
  val file = args(0)



    val wikiSchema = Encoders.product[WikiData].schema

  val df = sparkSession.read
      .schema(wikiSchema)
    .format("com.databricks.spark.xml").option("rowTag", "page").load(file)

  import sparkSession.implicits._

  val reqCols = df.select($"id".alias("id"), $"revision.contributor.id".alias("contributor_id"), $"revision.contributor.username".alias("contributor_name"),
    $"revision.text._VALUE".alias("text"), split($"revision.timestamp", "T")(0).alias("date"),substring($"revision.contributor.username",0,1).alias("alphaNum")).filter($"contributor_name".rlike("^[a-zA-Z0-9]*$"))

  reqCols.write.mode("overwrite").partitionBy("alphaNum").parquet("/user/itv000273/processed/wiki/wtiki.parquet")
}
}
