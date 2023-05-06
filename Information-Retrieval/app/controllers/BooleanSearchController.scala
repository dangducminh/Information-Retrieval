package controllers

import models.BuildForm
import play.api.libs.json.Json
import play.api.mvc.{BaseController, ControllerComponents}
import service.SearchService

import javax.inject._

/**
 * @author Minh Dang
 * @Date 4/16/2023
 */


@Singleton
class BooleanSearchController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

  private val searchService = new SearchService()

  def search(keyWord: String) = Action {
    val results = searchService.search(keyWord)
    if (results.isEmpty) {
      NoContent
    } else {
      Ok(Json.toJson(results))
    }
  }

  def searchAnd(keyWord1: String, keyWord2: String) = Action{
    val results = searchService.searchAnd(keyWord1,keyWord2)
    if (results.isEmpty) {
      NoContent
    } else {
      Ok(Json.toJson(results))
    }
  }

  def searchOr(keyWord1: String, keyWord2: String) = Action{
    val results = searchService.searchOr(keyWord1, keyWord2)
    if (results.isEmpty) {
      NoContent
    } else {
      Ok(Json.toJson(results))
    }
  }

  def searchAndNot(keyWord1: String, keyWord2: String) = Action{
    val results = searchService.searchAndNot(keyWord1, keyWord2)
    if (results.isEmpty) {
      NoContent
    } else {
      Ok(Json.toJson(results))
    }
  }

  def searchOrNot(keyWord1: String, keyWord2: String) = Action{
    val results = searchService.searchOrNot(keyWord1, keyWord2)
    if (results.isEmpty) {
      NoContent
    } else {
      Ok(Json.toJson(results))
    }
  }

  implicit val buildFormJson = Json.format[BuildForm]

  def build() = Action { implicit request =>
    val content = request.body
    val jsonObject = content.asJson

    val buildForm: Option[BuildForm] = jsonObject.flatMap(Json.fromJson[BuildForm](_).asOpt)

    buildForm match {
      case Some(newItem) =>
        val result = searchService.build(newItem.pathInput, newItem.pathOutput)
        if (result.isEmpty) {
          NoContent
        } else {
          Ok(Json.toJson(result))
        }
    }
  }

}
