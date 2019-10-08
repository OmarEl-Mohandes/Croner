package com

object Croner {
  final val parsers = List(Minute, Hour, DayOfMonth, Month, DayOfWeek, Command)

  def main(args: Array[String]): Unit = {

    require(args.length == parsers.length, s"Found: ${args.toList}\nPlease provide all ${parsers.size} parameters: ${parsers.map(_.toString).mkString(", ")}")

    val result = parsers.zipWithIndex.map {
      case (parser, ind) => parser -> parser.eval(args(ind))
    }

    require(result.forall(_._2.nonEmpty), s"Couldn't parse the input ${args.toList}")

    result.foreach(e => e._1.printFormated(e._2))
  }
}
