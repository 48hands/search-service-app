package views.forms

import java.time.LocalDate

/**
 * 検索フォーム
 *
 * @param dateStart 検索開始年月日
 * @param dateEnd 検索終了年月日
 * @param keyword 検索キーワード
 * @param male
 * @param female
 * @param company
 * @param receptionCode
 * @param model
 */
case class SearchForm(
  dateStart: Option[String],
  dateEnd: Option[String],
  keyword: Option[String],
  male: Option[Boolean],
  female: Option[Boolean],
  company: Option[Boolean],
  receptionCode: Option[String],
  model: Option[String])
