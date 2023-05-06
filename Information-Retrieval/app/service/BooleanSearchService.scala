package service

/**
 * @author Minh Dang
 * @Date 4/16/2023
 */
class BooleanSearchService {

  /**
   * search for documents containing both words
   *
   * @param corpus  the corpus containing all documents
   * @param keyWord the keywords want to find
   * @return an array of documents containing keyword
   */
  def search(corpus: Map[String, Array[String]], keyWord: String): Array[String] = {
    corpus.getOrElse(keyWord, Array.empty)
  }

  /**
   * search for documents containing both words
   *
   * @param corpus   the corpus containing all documents
   * @param keyWord1 the keywords want to find
   * @param keyWord2 the keywords want to find
   * @return an array of documents containing keyword
   */
  def searchAnd(corpus: Map[String, Array[String]], keyWord1: String, keyWord2: String): Array[String]  = {
    val doc1 = search(corpus, keyWord1)
    val doc2 = search(corpus, keyWord2)
    doc1.intersect(doc2)
  }

  /**
   * search for documents containing 1 of 2 words
   *
   * @param corpus   the corpus containing all documents
   * @param keyWord1 the keywords want to find
   * @param keyWord2 the keywords want to find
   * @return an array of documents containing keyword
   */
  def searchOr(corpus: Map[String, Array[String]], keyWord1: String, keyWord2: String): Array[String] = {
    val doc1 = search(corpus, keyWord1)
    val doc2 = search(corpus, keyWord2)
    (doc1 ++ doc2).distinct
  }

  /**
   * search for documents containing keyword 1 and not containing keyword 2
   *
   * @param corpus   the corpus containing all documents
   * @param keyWord1 the keywords want to find
   * @param keyWord2 the keywords want to find
   * @return an array of documents containing keyword
   */
  def searchAndNot(corpus: Map[String, Array[String]], keyWord1: String, keyWord2: String): Array[String]  = {
    val doc1 = search(corpus, keyWord1)
    val doc2 = search(corpus, keyWord2)
    doc1.filter(!doc2.contains(_))
  }

  /**
   * search for documents containing keyword 1 or not containing keyword 2
   *
   * @param corpus   the corpus containing all documents
   * @param keyWord1 the keywords want to find
   * @param keyWord2 the keywords want to find
   * @return an array of documents containing keyword
   */
  def searchOrNot(corpus: Map[String, Array[String]], keyWord1: String, keyWord2: String): Array[String] = {
    val doc1 = search(corpus, keyWord1)
    val doc2 = search(corpus, keyWord2)
    val flat = corpus.values.flatten.toArray
    (doc1 ++ flat.diff(doc2)).distinct
  }
}
