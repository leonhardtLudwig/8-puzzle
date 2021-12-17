import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Stack;

public class Solver {

    public static class BoardComparator implements Comparator<Board> {

        @Override
        public int compare(Board b1, Board b2) {
            int b1Priority = b1.manhattan() + b1.moves();
            int b2Priority = b2.manhattan() + b2.moves();
            if (b1Priority < b2Priority)
                return -1;
            else if (b1Priority > b2Priority)
                return 1;
            return 0;
        }

    }

    public static void main(String[] args) throws FileNotFoundException {
        //Scanner in = new Scanner(new File("instanceN3M4.txt"));
        Scanner in = new Scanner(new File(args[0]));
        int n = in.nextInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.nextInt();

        PriorityQueue<Board> pq = new PriorityQueue<Board>(new BoardComparator());
        //////////////////////////////
        // controlla se è risolvibile//
        //////////////////////////////
        Board initialBoard = new Board(tiles);
        pq.add(initialBoard);
        List<Board> neighbors = getNeighbor(initialBoard);
        Iterator<Board> it = neighbors.listIterator();
        while (it.hasNext()) {
            pq.add(it.next());
        }
        Board minPriority = pq.remove();
        while (!pq.isEmpty()&&minPriority.manhattan()!=0){
            minPriority = pq.remove();
            neighbors = getNeighbor(minPriority);
            it = neighbors.listIterator();
            while (it.hasNext()) {
                pq.add(it.next());
            }
        }
        //System.out.println(minPriority.toString());
        Stack<Board> path = new Stack<Board>();
        Board pathPiece = minPriority;
        System.out.println(minPriority.moves());
        while(pathPiece!=null){
            path.push(pathPiece);
            pathPiece = pathPiece.parent();
        }
        while(!path.isEmpty()){
            System.out.println(path.pop());
        }
    }

    public static List<Board> getNeighbor(Board board) {
        List<Board> b = new ArrayList<Board>();
        int[] blankPos = { -1, -1 };
        boolean foundBlank = false;
        int dim = board.dimension();
        int[][] tiles = board.tiles();
        for (int row = 0; row < dim && !foundBlank; row++) {
            for (int col = 0; col < dim && !foundBlank; col++) {
                foundBlank = tiles[row][col] == 0;
                if (foundBlank) {
                    blankPos[0] = row;
                    blankPos[1] = col;
                }
            }
        }
        String[] possibileMoves = { "down", "up", "right", "left" };
        if (blankPos[0] == 0)
            possibileMoves[0] = ""; // can't move down
        if (blankPos[0] == dim - 1)
            possibileMoves[1] = "";// can't move up
        if (blankPos[1] == 0)
            possibileMoves[2] = "";// can't move right
        if (blankPos[1] == dim - 1)
            possibileMoves[3] = ""; // can't move left
        for (int i = 0; i < possibileMoves.length; i++) {
            int[][] t = matrixCopy(tiles);
            Board leaf;
            switch (possibileMoves[i]) {
            case "down":
                t[blankPos[0]][blankPos[1]] = t[blankPos[0] - 1][blankPos[1]];
                t[blankPos[0] - 1][blankPos[1]] = 0;
                leaf = new Board(t, board);
                if (leaf.notEqualsAncient())
                    b.add(leaf);
                break;
            case "up":
                t[blankPos[0]][blankPos[1]] = t[blankPos[0] + 1][blankPos[1]];
                t[blankPos[0] + 1][blankPos[1]] = 0;
                leaf = new Board(t, board);
                if (leaf.notEqualsAncient())
                    b.add(leaf);
                break;
            case "right":
                t[blankPos[0]][blankPos[1]] = t[blankPos[0]][blankPos[1] - 1];
                t[blankPos[0]][blankPos[1] - 1] = 0;
                leaf = new Board(t, board);
                if (leaf.notEqualsAncient())
                    b.add(leaf);
                break;
            case "left":
                t[blankPos[0]][blankPos[1]] = t[blankPos[0]][blankPos[1] + 1];
                t[blankPos[0]][blankPos[1] + 1] = 0;
                leaf = new Board(t, board);
                if (leaf.notEqualsAncient())
                    b.add(leaf);
                break;
            case "":
                break;
            }
        }
        return b;
    }

    public static int[][] matrixCopy(int[][] tiles) {
        int[][] myInt = new int[tiles.length][];
        for (int i = 0; i < tiles.length; i++) {
            int[] aMatrix = tiles[i];
            int aLength = aMatrix.length;
            myInt[i] = new int[aLength];
            System.arraycopy(aMatrix, 0, myInt[i], 0, aLength);
        }
        return myInt;
    }

    // find a solution to the initial board (using the A* algorithm)

}
