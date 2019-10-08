package com

import org.scalatest.FlatSpec

class CronerParserSpec extends FlatSpec {

  final val parsers = List(Minute, Hour, DayOfMonth, Month, DayOfWeek)
  final val limits = List((0, 59), (0, 23), (1, 30), (1, 12), (1, 7))

  parsers.zipWithIndex.foreach {
    case (parser, ind) =>
      s"$parser" should s"evaluate * as all possible values from ${limits(ind)._1} to ${limits(ind)._2}" in {
        assertResult((limits(ind)._1 to limits(ind)._2).toList.map(_.toString))(parser.eval("*"))
      }

      s"$parser" should s"evaluate */3 to be all values within the limits" in {
        assert(parser.eval("*/3").forall(v => v.toInt >= limits(ind)._1 && v.toInt <= limits(ind)._2))
      }

      s"$parser" should s"evaluate 3-10 to be all values within the limits" in {
        assert(parser.eval("3-10").forall(v => v.toInt >= limits(ind)._1 && v.toInt <= limits(ind)._2))
      }

      s"$parser" should s"evaluate 3,10 to be all values within the limits" in {
        assert(parser.eval("3,10").forall(v => v.toInt >= limits(ind)._1 && v.toInt <= limits(ind)._2))
      }

      s"$parser" should s"evaluate 3,5000 and filter out the 5000" in {
        assertResult(List("3"))(parser.eval("3,5000"))
      }

      s"$parser" should s"evaluate 3,invalid and return empty list" in {
        assertResult(List())(parser.eval("3,invalid"))
      }
  }

  "Command eval" should "return the same input as it is" in {
    val command = "/bin/exec"
    assertResult(List(command))(Command.eval(command))
  }

  "Hour" should "return the exact range from 15-21" in {
    assertResult((15 to 21).toList.map(_.toString))(Hour.eval("15-21"))
  }
}
