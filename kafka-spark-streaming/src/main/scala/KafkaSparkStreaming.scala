package io.kafkaspark

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord}

import org.apache.spark.SparkConf
import org.apache.spark.streaming._
import org.apache.spark.streaming.kafka010._
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.serialization.StringDeserializer

import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.annotation.JsonProperty
case class DivvyStation(
  geopoint:String, 
  name:String, 
  id: Int)
case class DivvyUser(
  gender:String, 
  @JsonProperty("type") usertype:String, 
  birth_year: Int)
case class DivvyTrip(
  tripId:Int, 
  from_station:DivvyStation, 
  to_station:DivvyStation,
  user:DivvyUser,
  bike_id:Int,
  start_time:Long,
  end_time:Long,
  trip_duration:Int)

object Main extends App {
  if (args.length < 2) {
    System.err.println("Usage: io.kafkaspark.KafkaWordCount brokers topics")
    System.exit(1)
  }

  val Array(brokers, topics) = args

  Thread.sleep(5)

  // println(map)

  val sparkConf = new SparkConf().setAppName("KafkaWordCount").setMaster("local[4]")

  val ssc = new StreamingContext(sparkConf, Seconds(2))
  ssc.sparkContext.setLogLevel("ERROR")
  ssc.checkpoint("checkpoint")

  val groupId = s"spark_demo_${scala.util.Random.nextInt(100000)}"

  println(groupId)

  val kafkaParams = Map[String, Object](
    "bootstrap.servers" -> brokers,
    "key.deserializer" -> classOf[StringDeserializer],
    "value.deserializer" -> classOf[StringDeserializer],
    "group.id" -> groupId,
    "auto.offset.reset" -> "latest",
    "enable.auto.commit" -> (false: java.lang.Boolean)
  )

  val stream = KafkaUtils.createDirectStream[String, String](
    ssc,
    PreferConsistent,
    Subscribe[String, String](topics.split(","), kafkaParams)
  )

  val usertypes = stream.map { record =>
    val mapper = new ObjectMapper with ScalaObjectMapper
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    mapper.registerModule(DefaultScalaModule)
    mapper.readValue[DivvyTrip](record.value)
  }.map { trip => trip.user.usertype }
  val typecounts = usertypes.countByValue()
  typecounts.print()

  ssc.start()

  ssc.awaitTermination()
 }