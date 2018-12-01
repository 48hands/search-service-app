package controllers

import java.io.{BufferedWriter, FileWriter }
import java.time.LocalDate
import java.time.format.DateTimeFormatter

import domain.models.ReceptionHistory
import domain.repositories.ReceptionHistoryRepository
import javax.inject.Inject
import play.api.mvc._

import scala.concurrent.{ Await, Future }
import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{ Failure, Success }

class GenerateDataController @Inject() (cc: ControllerComponents, repository: ReceptionHistoryRepository) extends AbstractController(cc) {

  def insertReceptionHistory = Action.async(parse.json) { implicit request =>

    val records = (request.body \ "records").as[Int]
    val dateString = (request.body \ "date").as[String]
    val yearMonth = dateString.substring(0, 6)

    val f = Future.sequence {
      for (i <- 1 to records) yield {
        repository.insert(
          ReceptionHistory(
            receptionNo = i.toString,
            searchYearMonth = yearMonth,
            receptionYmd = dateString,
            accept = scala.util.Random.alphanumeric.take(200).mkString
          )
        )
      }
    }

    Await.ready(f, Duration.Inf)

    Future {
      f.value.get match {
        case Success(histories) =>
          Ok(s"${histories.length} records inserted.")
        case Failure(e) =>
          Ok(e.toString)
      }
    }
  }

  def fetchReceptionHistory = Action { implicit request =>

    val start = LocalDate.parse("20180101", DateTimeFormatter.ofPattern("yyyyMMdd"))
    val end = LocalDate.parse("20180107", DateTimeFormatter.ofPattern("yyyyMMdd"))

    val f = repository.fetch(start, end)

    Await.ready(f, Duration.Inf)
    f.value.get match {
      case Success(receptionHistories) =>
        val bw = new BufferedWriter(new FileWriter("Samplefile.txt"))
        try {
          receptionHistories.foreach { record =>
            bw.write(s"${record.receptionNo}, ${record.receptionYmd}, ${record.searchYearMonth}, ${record.accept}\n")
          }
        } finally {
          bw.close()
        }
        Ok(s"${receptionHistories.length} records")

      case Failure(exception) =>
        Ok(s"${exception.getMessage}")
    }

  }

}
