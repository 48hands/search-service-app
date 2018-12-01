package models

import java.time.LocalDate

case class ReceptionHistoryRecord(id: Option[Long], body: String, createdAt: LocalDate, updatedAt: LocalDate)
