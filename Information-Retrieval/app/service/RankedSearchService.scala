package service

import org.tartarus.snowball.ext.englishStemmer
import scala.math.pow

/**
 * @author Minh Dang
 * @Date 5/1/2023
 */
class RankedSearchService {

  val corpusIndexing = new CorpusIndexing

  /**
   * split and stemming query
   * after that compute score query using the Tf-Idf algorithm
   * and match scores by cosine similarity
   * and take the 20 docs with the highest score
   *
   * @param vocabs vocabs single term for all docs
   * @param vectorTf_Idf the array where key is the document , value is array of score
   * @param keyWord word or sentence to find
   * @return 20 docs with the highest score
   */
  def search(vocabs: Map[String, Array[String]],vectorTf_Idf:Array[(String, Array[Double])] , keyWord: String): Array[String] = {
    val stemmer = new englishStemmer()
    val query = Map("q"->corpusIndexing.stem(keyWord.split("""[,.;&><()/ :”“"\s]+""").map(child => child.toLowerCase()),stemmer).toArray)
    val vectorQuery = corpusIndexing.termFrequency_InverseDocumentFrequencyVectorized(vocabs.asInstanceOf[Map[String,Array[Any]]],query,vectorTf_Idf.size+1)
    computeCosine(vectorTf_Idf,vectorQuery).map(_._1)
  }

  /**
   * match scores by cosine similarity and take the 20 docs with the highest score
   * formula : Cos(x, y) = x . y / ||x|| * ||y||
   *
   * @param vectorTf_Idf
   * @param vectorQuery
   * @return 20 docs with the highest score
   */
  private def computeCosine(vectorTf_Idf:Array[(String, Array[Double])],vectorQuery:Array[(String, Array[Double])]): Array[(String,Double)] = {
    val vocabSize = vectorQuery(0)._2.size

    vectorTf_Idf.map(vector =>{
      var scalarProduct = 0.0
        for (i <- 0 to vocabSize-1){
          scalarProduct += vectorQuery(0)._2(i) * vector._2(i)
        }
      val lengthOfVectorDoc = vector._2.map(pow(2,_)).sum
      val lengthOfVectorQuery = vectorQuery(0)._2.map(pow(2,_)).sum
      val productOfLength = lengthOfVectorQuery * lengthOfVectorDoc

      (vector._1,scalarProduct/productOfLength)
    }).sortBy(_._2).reverse.take(20)
  }

}
