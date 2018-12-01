package models

import java.time.LocalDateTime

case class SearchStatusRecord(
  requestId: Long,
  status: String,
  requestedAt: LocalDateTime)
