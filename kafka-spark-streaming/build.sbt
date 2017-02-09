name := "KafkaSparkStreaming"

version := "0.1"

scalaVersion := "2.11.8"

assemblyJarName in assembly := "uber.jar"

libraryDependencies ++= 
  Seq("org.apache.spark" %% "spark-core" % "2.0.2",   
      "org.apache.spark" %% "spark-sql"  % "2.0.2",
      "org.apache.spark" %% "spark-streaming" % "2.0.2",
      "org.apache.spark" %% "spark-streaming-kafka-0-10" % "2.1.0",
      "org.apache.kafka" %% "kafka" % "0.10.1.1",
      "org.apache.kafka" % "kafka-clients" % "0.10.1.1",
      "com.fasterxml.jackson.core" % "jackson-databind" % "2.8.6",
      "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.8.6")

mergeStrategy in assembly <<= (mergeStrategy in assembly) { (old) =>
  {
    case n if n.endsWith("org.apache.hadoop.fs.FileSystem") => MergeStrategy.concat
    case PathList("META-INF", xs @ _*) => MergeStrategy.discard
    case n if n.startsWith("reference.conf") => MergeStrategy.concat
    case x => MergeStrategy.first
  }
}
