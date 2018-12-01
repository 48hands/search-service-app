package domain.repositories

import java.time.LocalDate

import domain.models.ReceptionHistory

import scala.concurrent.Future

trait ReceptionHistoryRepository {

  def fetch(start: LocalDate, end: LocalDate): Future[Seq[ReceptionHistory]]

  def insert(receptionHistory: ReceptionHistory): Future[Long]
}
