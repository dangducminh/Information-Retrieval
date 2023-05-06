package service

/**
 * @author Minh Dang
 * @Date 4/16/2023
 */
class SearchService {

  private var started = false
  private var vocabSingleTermBooleanSearch: Map[String, Array[String]] = null.asInstanceOf[Map[String, Array[String]]]
  private var vocabDoubleTermBooleanSearch: Map[String, Array[String]] = null.asInstanceOf[Map[String, Array[String]]]
  private var vocabBooleanSearch: Map[String, Array[String]] = null.asInstanceOf[Map[String, Array[String]]]
  private var vocabRankedSearch: Array[(String, Array[Double])] = null.asInstanceOf[Array[(String, Array[Double])] ]
  private val booleanSearchService = new BooleanSearchService
  private val rankedSearchService = new RankedSearchService
  private val corpusIndexing = new CorpusIndexing

  private def startApp() = {
    started = true
    vocabSingleTermBooleanSearch = corpusIndexing.readVocabsBooleanSearch("vocab_single_term_boolean_search.txt")
    vocabDoubleTermBooleanSearch = corpusIndexing.readVocabsBooleanSearch("vocab_double_term_boolean_search.txt")
    vocabBooleanSearch = vocabSingleTermBooleanSearch++vocabDoubleTermBooleanSearch
    vocabRankedSearch = corpusIndexing.readVocabRankedSearch()
  }

  def search(keyWord: String): Array[String] = {
    if (!started) startApp()
    booleanSearchService.search(vocabBooleanSearch,keyWord)
  }

  def searchAnd(keyWord1: String, keyWord2: String): Array[String] = {
    if (!started) startApp()
    booleanSearchService.searchAnd(vocabBooleanSearch, keyWord1, keyWord2)
  }

  def searchOr(keyWord1: String, keyWord2: String): Array[String] = {
    if (!started) startApp()
    booleanSearchService.searchOr(vocabBooleanSearch, keyWord1, keyWord2)
  }

  def searchAndNot(keyWord1: String, keyWord2: String): Array[String] = {
    if (!started) startApp()
    booleanSearchService.searchAndNot(vocabBooleanSearch, keyWord1, keyWord2)
  }

  def searchOrNot(keyWord1: String, keyWord2: String): Array[String] = {
    if (!started) startApp()
    booleanSearchService.searchOrNot(vocabBooleanSearch, keyWord1, keyWord2)
  }
  def build(pathInput: String, pathOutput: String): String = {
      corpusIndexing.build()
  }

  def rankedSearch(keyWord: String): Array[String] = {
    if (!started) startApp()
    rankedSearchService.search(vocabSingleTermBooleanSearch,vocabRankedSearch, keyWord)
  }

}
