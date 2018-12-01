package infrastracture.repositories

import java.time.LocalDate

import scalikejdbc._
import domain.models.ReceptionHistory
import domain.repositories.ReceptionHistoryRepository
import javax.inject.Inject
import utils.ImplicitUtils._

import scala.concurrent.{ ExecutionContext, Future }

class ReceptionHistoryRepositoryImpl @Inject() ()(implicit ec: ExecutionContext) extends ReceptionHistoryRepository {

  override def fetch(start: LocalDate, end: LocalDate): Future[Seq[ReceptionHistory]] = Future {
    DB.readOnly { implicit session =>
      sql"""
        select
          reception_no,
          search_ym,
          reception_ymd,
          accepts_1
        from reception_history
        where reception_ymd >= ${start.yyyyMMdd} and reception_ymd <= ${end.yyyyMMdd}
        """
        .map(rs => mappingRecord(rs))
        .list()
        .apply()
    }
  }

  override def insert(receptionHistory: ReceptionHistory): Future[Long] = Future {
    DB.localTx { implicit session =>
      sql"""
        insert into reception_history (reception_no, search_ym, reception_ymd, accepts_1)
        values (
          ${scala.util.Random.alphanumeric.take(10).mkString},
          ${receptionHistory.searchYearMonth},
          ${receptionHistory.receptionYmd},
          ${receptionHistory.accept}
        )
        """.update.apply()
    }
  }

  private def mappingRecord(rs: WrappedResultSet): ReceptionHistory = {
    ReceptionHistory(
      rs.stringOpt("reception_no").getOrElse("0"),
      rs.stringOpt("search_ym").getOrElse("0"),
      rs.stringOpt("reception_ymd").getOrElse("0"),
      rs.stringOpt("accepts_1").getOrElse("0")
    )
  }
}
