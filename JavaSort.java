package tetris;

import java.util.Random;

public class JavaSort {

    static Random rand = new Random();

    /**
     * quicksort
     *
     * @param begin border
     * @param end   border
     */
    public void qSort(Notation[] array, int begin, int end) {
        int i = begin;
        int j = end;
        int x = array[begin + rand.nextInt(end - begin + 1)].getScore();
        while (i <= j) {
            while (array[i].getScore() > x) {
                i++;
            }
            while (array[j].getScore() < x) {
                j--;
            }
            if (i <= j) {
                Notation temp = array[i];
                array[i] = array[j];
                array[j] = temp;
                i++;
                j--;
            }
        }
        if (begin < j) {
            qSort(array, begin, j);
        }
        if (i < end) {
            qSort(array, i, end);
        }
    }
}