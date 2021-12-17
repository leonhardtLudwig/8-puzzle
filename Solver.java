import java.io.File;
import java.io.FileNotFoundException;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Solver {

    public static class BoardComparator implements Comparator<Board> {

        @Override
        public int compare(Board b1, Board b2) {
            int b1Priority = b1.manhattan() + b1.moves();
            int b2Priority = b2.manhattan() + b2.moves();
            if (b1Priority < b2Priority)
                return 1;
            else if (b1Priority>b2Priority)
                return -1;
            return 0;
        }

    }

    public static void main(String[] args) throws FileNotFoundException {
        // create initial board from file
        // Scanner in = new Scanner(new File(args[0]));
        Scanner in = new Scanner(new File("instanceN3M4.txt"));
        int n = in.nextInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.nextInt();

        PriorityQueue<Board> pq = new PriorityQueue<Board>(new BoardComparator());
        pq.add(new Board(tiles));
    }

    // find a solution to the initial board (using the A* algorithm)

}
