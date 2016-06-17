package tetris;

/** QSort on Scala */
class ScalaSort {
  def sort(gameArray: Array[Notation]) {
    def swap(a: Int, b: Int) {
      val temp = gameArray(a);
      gameArray(a) = gameArray(b)
      gameArray(b) = temp
    }

    def qSort(begin: Int, end: Int) {
      val temp = gameArray((begin + end) / 2).getScore
      var i = begin
      var j = end
      while (i <= j) {
        while (gameArray(i).getScore > temp) {
          i += 1
        }
        while (gameArray(j).getScore < temp) {
          j -= 1
        }
        if (i <= j) {
          swap(i, j)
          i += 1
          j -= 1
        }
      }
      if (begin < j) qSort(begin, j)
      if (j < end) qSort(i, end)
    }
    qSort(0, gameArray.length - 1)
  }
}


