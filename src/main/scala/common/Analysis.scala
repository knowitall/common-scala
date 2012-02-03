package common.stats

object Analysis {
  /**
    * Compute precision yield points for each yield value.
    */
  def precisionYield(scores: Seq[Boolean]) = {
    var correct = 0
    var incorrect = 0
    var points = List[(Int, Double)]()
    
    for (score <- scores) {
      if (score) correct = correct + 1
      else incorrect = incorrect + 1
      
      if (score) {
        points ::= (correct, precision(correct, incorrect))
      }
    }
    
    points.reverse
  }
  
  /**
    * Compute precision from counts of correct and incorrect examples.
    */
  def precision(correct: Int, incorrect: Int): Double = 
    (correct.toDouble) / (correct + incorrect).toDouble
    
  /**
    * Compute precision from a series of evaluations.
    */
  def precision(scores: Seq[Boolean]): Double = {
    val correct = scores.count(_ == true)
    precision(correct, scores.size - correct)
  }
}