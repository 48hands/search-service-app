package controllers

import java.time.{ LocalDate, LocalDateTime }
import java.io.File
import java.nio.file.{ Files, Paths }

import javax.inject.Inject

import scala.concurrent.ExecutionContext.Implicits._
import play.api.i18n.I18nSupport
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.libs.json._
import views.forms.SearchForm

import models.{ ReceptionHistoryRecord, SearchConditionRecord, SearchStatusRecord }

class HistorySearchController @Inject() (mcc: MessagesControllerComponents) extends MessagesAbstractController(mcc) with I18nSupport {

  private def searchForm = Form {
    mapping(
      "dateStart" -> optional(text),
      "dateEnd" -> optional(text),
      "keyword" -> optional(text),
      "male" -> optional(boolean),
      "female" -> optional(boolean),
      "company" -> optional(boolean),
      "receptionCode" -> optional(text),
      "model" -> optional(text)
    )(SearchForm.apply)(SearchForm.unapply)
  }

  private def getOptValue(query: Map[String, Seq[String]], key: String): Option[String] =
    query.get(key).headOption match {
      case Some(value) => value.headOption
      case _ => None
    }

  private def cachedSearchForm(query: Map[String, Seq[String]]) = SearchForm(
    getOptValue(query, "dateStart"),
    getOptValue(query, "dateStart"),
    getOptValue(query, "keyword"),
    getOptValue(query, "male").map(_.toBoolean),
    getOptValue(query, "female").map(_.toBoolean),
    getOptValue(query, "company").map(_.toBoolean),
    getOptValue(query, "receptionCode"),
    getOptValue(query, "model")
  )

  /**
   * 応対履歴検索
   */
  def index: Action[AnyContent] = Action { implicit request =>
    val query: Map[String, Seq[String]] = request.queryString

    // TODO 検索サービスを実行する。
    val receptionHistories = for (receptionNo <- 1000000000 to 1000000200) yield {
      ReceptionHistoryRecord(
        Option(receptionNo),
        "TEST-%10d お客様申告お客様申告お客様申告お客様申告お客様申告お客様申告お客様申告お客様申告お客様申告お客様申告お客様申告お客様申告お客様申告お客様申告お客様申告お客様申告お客様申告お客様申告お客様申告お客様申告".format(receptionNo),
        LocalDate.now,
        LocalDate.now)
    }

    Ok(views.html.search(receptionHistories,
      searchForm.fill(cachedSearchForm(query))))
  }

  def getSearchCondition(id: Long) = Action { implicit request =>
    import HistorySearchController._

    println(request.queryString)

    // TODO idを受け取ってDBを検索してフォームを埋めるようにする。
    val searchForm = SearchForm(
      dateStart = Option("01-12-2020"),
      dateEnd = Option("01-12-2020"),
      keyword = Option("ほげほげほげ"),
      male = Option(true),
      female = Option(false),
      company = Option(true),
      receptionCode = Option("8888"),
      model = Option("iPhone12")
    )
    // JSONで返却する。
    Ok(Json.toJson(searchForm))
  }

  /**
   * 検索結果をダウンロードするアクション
   */
  def download = Action {
    // TODO 応対履歴の検索結果をファイル化する。
    val file = Files.createTempFile(Paths.get("/tmp"), "prefix", "suffix").toFile
    Ok.sendFile(file)
  }

  /**
   * 加入者一覧を受け取って加入者に紐づく
   * 応対履歴を検索する。
   */
  def sbsUploadAndSearch = TODO

  /**
   * 加入者一覧紐づけ検索画面表示アクション
   */
  def sbsUploadShow = Action { implicit request =>
    Ok(views.html.sbsSearch(searchForm))
  }

  /**
   * 検索条件をDBから取得するアクション
   */
  def searchConditionList = Action { implicit request =>
    // TODO DBから検索条件を取得する
    val searchConditions = for (searchId <- 1000 to 1200) yield {
      SearchConditionRecord(searchId,
        LocalDateTime.now.plusSeconds(searchId),
        "設定1",
        "設定2",
        "設定3",
        "設定4",
        "設定5",
        "設定6",
        "設定7",
        "設定8"
      )
    }

    // 作成日時の降順でソートしてviewに渡す。
    Ok(views.html.searchConditionList(
      searchConditions.sortWith((a, b) => a.createdAt.isAfter(b.createdAt))))
  }

  /**
   * 検索結果一覧画面のアクション
   */
  def getResults = Action { implicit request =>
    val searchStates = for (requestId <- 2000 to 2200) yield {
      SearchStatusRecord(
        requestId,
        Vector("処理中", "完了")(requestId % 2),
        LocalDateTime.now.plusSeconds(requestId)
      )
    }
    Ok(views.html.results(searchStates))
  }
}

object HistorySearchController {
  implicit val writes: Writes[SearchForm] = (searchForm: SearchForm) => {
    Json.obj(
      "dateStart" -> searchForm.dateStart,
      "dateEnd" -> searchForm.dateEnd,
      "keyword" -> searchForm.keyword,
      "male" -> searchForm.male,
      "female" -> searchForm.female,
      "company" -> searchForm.company,
      "receptionCode" -> searchForm.receptionCode,
      "model" -> searchForm.model
    )
  }
}