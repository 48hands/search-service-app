package models

import java.time.LocalDateTime

case class SearchConditionRecord(
                                  searchId: Long,
                                  createdAt: LocalDateTime,
                                  condition1: String,
                                  condition2: String,
                                  condition3: String,
                                  condition4: String,
                                  condition5: String,
                                  condition6: String,
                                  condition7: String,
                                  condition8: String,
                                )
