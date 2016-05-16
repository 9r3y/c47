package com.y3r9.c47.dog

import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._

import scala.beans.BeanProperty

/**
  * Created by Ethan.Zhou on 2016/4/13.
  */
object WordCount {

  def main(args: Array[String]) {
    val inputPath = args(0)
    val outputPath = args(1)
    val sc = new SparkContext()
    val lines = sc.textFile(inputPath)
    val wordCounts = lines.flatMap {line => line.split(" ")}
      .map(word => (word, 1))
      .reduceByKey(_ + _)
    wordCounts.saveAsTextFile(outputPath)
    println(wordCounts.first)


    val wtext = sc.wholeTextFiles("redis/src/*.[ch]")
    val fwds = wtext.
      flatMap{ case (filename, contents) =>
        val fname = filename.substring(filename.lastIndexOf("/") + 1)
        contents.
          split("\\W+").
          filter(!_.isEmpty).
          map( word => (fname, word))
      }
    fwds.cache()
    val f = (1 to 9).map(_ * 8)

  }


}
