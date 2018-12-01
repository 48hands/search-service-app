package tools

import java.time.{ LocalDate, Month }

import domain.models.ReceptionHistory

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{ Failure, Success }
import domain.repositories.ReceptionHistoryRepository
import infrastracture.repositories.ReceptionHistoryRepositoryImpl
import scalikejdbc.config._

import scala.concurrent.{ Await, Future }
import scala.concurrent.duration.Duration

object ReceptionHistoryLoadTool extends App {

  DBs.setupAll()

  val tool = new ReceptionHistoryLoadTool(new ReceptionHistoryRepositoryImpl())
  //  tool.fetch(
  //    LocalDate.of(2018, Month.NOVEMBER, 1),
  //    LocalDate.of(2018, Month.NOVEMBER, 30))

  tool.load(100, LocalDate.now())

}

class ReceptionHistoryLoadTool(repository: ReceptionHistoryRepository) {

  def load(record: Int, localDate: LocalDate) = {
    import utils.ImplicitUtils._

    val f = Future.sequence {
      for (i <- 1 to record) yield {
        repository.insert(
          ReceptionHistory(
            i.toString,
            localDate.yyyyMM,
            localDate.yyyyMMdd,
            "ほげほげほげほげほげほげほげほげほげ"
          )
        )
      }
    }
    Await.ready(f, Duration.Inf)
    f.onComplete {
      case Success(count) =>
        println(s"$count records inserted.")
      case Failure(e) =>
        e.printStackTrace()
    }
  }

  def fetch(start: LocalDate, end: LocalDate) = {
    Await.ready(repository.fetch(start, end), Duration.Inf)
      .foreach(receptionHistories =>
        println(receptionHistories.length))
  }

}