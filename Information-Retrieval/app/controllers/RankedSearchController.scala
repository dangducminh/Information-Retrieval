package controllers

import play.api.libs.json.Json
import play.api.mvc.{BaseController, ControllerComponents}
import service.SearchService

import javax.inject._

/**
 * @author Minh Dang
 * @Date 5/1/2023
 */

@Singleton
class RankedSearchController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

  private val searchService = new SearchService()

  def search(keyWord: String) = Action {
    val results = searchService.rankedSearch(keyWord)
    if (results.isEmpty) {
      NoContent
    } else {
      Ok(Json.toJson(results))
    }
  }

}
