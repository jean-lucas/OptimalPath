package StringSorts;

/**
 * Least Significant Digit (LSD) sort
 * Taken from Robert Sedgewick 4ed "Algorithms" page 707
 * @param a  array to be sorted
 * @param W	 size of string to be sorted
 * 
 */


public class LSDsort {


  // LSD radix sort
  public void sort(String[] a, int W) {
  	int N = a.length;
    int R = 256;   // extend ASCII alphabet size
    String[] aux = new String[N];

    for (int d = W-1; d >= 0; d--) {
        // sort by key-indexed counting on dth character

        // compute frequency counts
        int[] count = new int[R+1];
        for (int i = 0; i < N; i++) {
            count[a[i].charAt(d) + 1]++;
        }
        // compute cumulates
        for (int r = 0; r < R; r++)
            count[r+1] += count[r];

        // move data
        for (int i = 0; i < N; i++)
            aux[count[a[i].charAt(d)]++] = a[i];

        // copy back
        for (int i = 0; i < N; i++)
            a[i] = aux[i];
    }
}

}
