package tetris

/**
 * Analysis of game statistics
 */
class ScalaStatistics {
  /**
   * Find count of every figure
   */
  def getStatistic(gameInfo: Array[Notation]){
    val tempArray = for (temp: Notation <- gameInfo) yield temp
    val statistic = new Array[Int](7)

    for (temp: Notation <- tempArray)
      statistic(temp.getType-1)+=1

    new StatisticsTable("Statistic", statistic, statistic.indexOf(statistic.max))
  }
}
