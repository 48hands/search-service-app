package utils

import java.time.LocalDate
import java.time.format._

object ImplicitUtils {

  implicit class RichLocalDate(localDate: LocalDate) {
    def yyyyMMdd: String = localDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"))

    def yyyyMM: String = localDate.format(DateTimeFormatter.ofPattern("yyyyMM"))
  }

}
