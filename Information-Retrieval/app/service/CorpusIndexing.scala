package service

import org.tartarus.snowball.ext.englishStemmer

import java.io.{BufferedWriter, File, FileWriter}
import scala.io.Source
import scala.math.log10

/**
 * @author Minh Dang
 * @Date 4/16/2023
 */
class CorpusIndexing {

  /**
   *
   * @param beforeStem the list the raw data to be converted back to the original word
   * @param stemmer    type of language you want to convert
   * @return the list word after being returned to the original word
   */
  def stem(beforeStem: Seq[String], stemmer: englishStemmer): List[String] = {
    beforeStem.map(item => {
      stemmer.setCurrent(item)
      if (stemmer.stem())
        stemmer.getCurrent()
      else null
    }).toList
  }

  /**
   * If the file is in the project, the relative path can be obtained from the src directory
   * if it is not, the absolute path must be provided
   * otherwise, the default path will be app\data
   * function split && stemming and build vocab :
   * boolean search with format : term   doc1 ... doc_n
   * ranked search built using the Tf-Idf algorithm and save with format : doc1 scoreTerm1 scoreTerm2 ... scoreTerm_n
   *
   * @param pathInput the path to file(optional)
   * @return success after build and save vocab to file
   */
  def build(pathInput: String = "app\\data" ,pathOutput: String = "result.txt"): String = {
    val filesList = new File(pathInput).listFiles()
    val rawData = filesList.map(pathItem =>
      (pathItem.getName,
        Source.fromFile(pathItem.toString, "iso-8859-1").getLines().filter(!_.equalsIgnoreCase("")).toList.mkString(" "))
    ).toMap

    val stemmer = new englishStemmer()

    var unigram = rawData.map(item => (item._1, item._2.split("""[,.;&><()/ :”“"\s]+""").map(child => child.toLowerCase())))
    var bigrams = unigram.map(item => {
      (item._1, item._2.sliding(2).toList.distinct.map(_.mkString(" ")).toArray)
    })
    unigram = unigram.map(item => (item._1, stem(item._2, stemmer).toArray))
    bigrams = bigrams.map(item => (item._1, stem(item._2, stemmer).toArray))

    val mapUnigramTerm = buildVocab(unigram)
    val mapBigramsTerm = buildVocab(bigrams)

    writeVocabForBooleanSearch(mapUnigramTerm ,"vocab_single_term_boolean_search.txt")
    writeVocabForBooleanSearch(mapBigramsTerm ,"vocab_double_term_boolean_search.txt")
    val vectorTf_Idf = termFrequency_InverseDocumentFrequencyVectorized(mapUnigramTerm,unigram,unigram.size)
    writeVocabForRankedSearch(vectorTf_Idf)
    return "Success"
  }

  /**
   * convert from format : doc1 -> term1 term2 ... term_n to term1 -> doc1 doc3 ... doc_n
   * convert from a document containing many words to a term will be associated with a list of documents containing it
   *
   * @param mapDocId
   * @return
   */
  private def buildVocab(mapDocId : Map[String,Array[String]]): Map[String, Array[Any]] = {
    val afterFlatten = mapDocId.values.flatten.toList.distinct
    afterFlatten.map(word => {
      (word,
        mapDocId.map(item => {
          if (item._2.contains(word)) {
            item._1
          }
        }).filter(_.getClass.getSimpleName == "String").toArray)
    }).toMap
  }

  /**
   * the result will be saved at vocab_single_term_boolean_search.txt with single term
   * and saved at vocab_single_term_boolean_search.txt with 2 term
   *
   * @param path path and file name
   * @param corpus the data needs to be stored.
   */
  private def writeVocabForBooleanSearch(corpus: Map[String, Array[Any]], path: String): Unit = {
    val file = new File(path)
    val bw = new BufferedWriter(new FileWriter(file))
    corpus.foreach(item => {
      bw.write(item._1 + "\t" + item._2.mkString(" ") + "\n")
    })
    bw.close()
    println("done task !")
    println("Results are saved in the root directory's %s file.".format(path))
  }


  /**
   * the result will be read at vocab_single_term_boolean_search.txt with single term
   * and read at vocab_single_term_boolean_search.txt with 2 term
   *
   * @param path path to read file
   * @return the map where key is the keyword value is an array of documents containing that keyword
   */
  def readVocabsBooleanSearch(path: String): Map[String, Array[String]] = {
    Source.fromFile(path).getLines.map(line => {
      val temp = line.split("\t")
      val keyWord = temp(0)
      val listDocs = temp(1).split(" ")
      (keyWord, listDocs)
    }).toMap
  }

  /**
   * For each word in vocab, it matches each word in the documents,
   * if it matches, it will calculate the score using the tf-idf algorithm
   * if it is not assigned a score of 0.0
   *
   * @param vocabs the map where key is the keyword value is an array of documents containing that keyword
   * @param docs the map where key is the document value is an array of term in this document
   * @param numberOfDocuments total number of documents
   * @return the array where key is the document , value is array of score using the Tf-Idf algorithm
   */
  def termFrequency_InverseDocumentFrequencyVectorized(vocabs:Map[String, Array[Any]], docs: Map[String, Array[String]], numberOfDocuments:Int): Array[(String, Array[Double])]  = {
    docs.map(doc =>{
      (doc._1,vocabs.map(term => {
        if (doc._2.contains(term._1)){
          doc._2.count(_==term._1)*1.0/doc._2.size * log10(numberOfDocuments/term._2.size)
        }else{
          0
        }
      }).toArray)
    }).toArray
  }

  /**
   * the result will be read at vocab_ranked_search.txt
   *
   * @param corpus the array where key is the document , value is array of score using the Tf-Idf algorithm
   * @param path path and file name
   */
  private def writeVocabForRankedSearch(corpus: Array[(String, Array[Double])] , path: String = "vocab_ranked_search.txt"): Unit = {
    val file = new File(path)
    val bw = new BufferedWriter(new FileWriter(file))
    corpus.foreach(item => {
      bw.write(item._1 + "\t" + item._2.mkString(" ") + "\n")
    })
    bw.close()
    println("done task !")
    println("Results are saved in the root directory's %s file.".format(path))
  }

  /**
   *
   * @param path path to read file
   * @return the array where key is the document , value is array of score using the Tf-Idf algorithm
   */
  def readVocabRankedSearch(path: String = "vocab_ranked_search.txt"): Array[(String, Array[Double])] = {
    Source.fromFile(path).getLines.map(line => {
      val temp = line.split("\t")
      val keyWord = temp(0)
      val listDocs = temp(1).split(" ").map(_.toDouble)
      (keyWord, listDocs)
    }).toArray
  }

}
