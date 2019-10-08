package com

import scala.util.Try
import scala.util.matching.Regex

object Croner {
  final val parsers = List(Minute, Hour, DayOfMonth, Month, DayOfWeek, Command)

  def main(args: Array[String]): Unit = {

    require(args.length == parsers.length, s"Please provide all ${parsers.size} parameters: ${parsers.map(_.toString).mkString(", ")}")

    val result = parsers.zipWithIndex.map {
      case (parser, ind) => parser -> parser.eval(args(ind))
    }

    assert(result.forall(_._2.nonEmpty), s"Couldn't parse the input ${args.toList}")

    result.foreach(e => e._1.printFormated(e._2))
  }
}



trait CronParser {
  protected val singleNumOrAsterixP: Regex = """([\d+\*])""".r
  protected val commaSeparatedP: Regex = """([\d,]+)""".r
  protected val slashSeparatedP: Regex = """([\d\*])/(\d+)""".r
  protected val dashSeparatedP: Regex = """([\d+\*])-([\d+\*])""".r
  protected val maxAllowedValue: Int = Integer.MAX_VALUE
  protected val minAllowedValue: Int = 0

  def printFormated(res: List[String]): Unit = {
    println(f"$toString%-14s${res.mkString(" ")}")
  }

  def eval(str: String): List[String] = {
    str match {
      case singleNumOrAsterixP(num) => evalNum(num)
      case commaSeparatedP(values) => evalCommaSeparated(values)
      case slashSeparatedP(initial, increment) => evalSlashSeparated(initial, increment)
      case dashSeparatedP(from, to) => evalDashSeparated(from, to)
      case _ => List()
    }
  }

  private def evalNum(num: String): List[String] = {
    num match {
      case "*" => (minAllowedValue to maxAllowedValue).toList.map(_.toString)
      case n if n.matches("""(\d+)""") => List(n)
      case _ => List()
    }
  }

  private def evalDashSeparated(from: String, to: String): List[String] = {
    Try {
      val fromInt: Int = evalNum(from).head.toInt
      val toInt: Int = evalNum(to).last.toInt
      getValuesByIncrement(fromInt, toInt, 1)
    } getOrElse List()
  }

  private def evalCommaSeparated(values: String): List[String] = values.split(",").toList

  private def evalSlashSeparated(a: String, b: String): List[String] = {
    Try {
      val from = evalNum(a).head.toInt
      val increment = b.toInt
      getValuesByIncrement(from, maxAllowedValue, increment)
    } getOrElse List()
  }

  private def getValuesByIncrement(from: Int, toMax: Int, increment: Int): List[String] = {
    if (from >= minAllowedValue && from <= maxAllowedValue && toMax <= maxAllowedValue) {
      (from to toMax by increment)
        .toList
        .map(_.toString)
    } else
      List()
  }

}

object Month extends CronParser {
  override protected val maxAllowedValue = 12
  override protected val minAllowedValue = 1
  override def toString: String = "month"
}

object DayOfMonth extends CronParser {
  override protected val maxAllowedValue = 30
  override protected val minAllowedValue = 1
  override def toString: String = "day of month"
}

object DayOfWeek extends CronParser {
  override protected val maxAllowedValue = 7
  override protected val minAllowedValue = 1
  override def toString: String = "day of week"
}

object Hour extends CronParser {
  override protected val maxAllowedValue = 23
  override protected val minAllowedValue = 0
  override def toString: String = "hour"
}

object Minute extends CronParser {
  override protected val maxAllowedValue = 59
  override protected val minAllowedValue = 0
  override def toString: String = "minute"
}

object Command extends CronParser {
  override def eval(str: String): List[String] = List(str)
  override def toString: String = "command"
}

