import java.io.*;
import java.util.*;

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
        //Scanner in = new Scanner(new File("unsolvableN4i1b3.txt"));
        Scanner in = new Scanner(new File(args[0]));
        int n = in.nextInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.nextInt();

        PriorityQueue<Board> pq = new PriorityQueue<Board>(new BoardComparator());
        if (isSolvable(tiles)) {
            Board initialBoard = new Board(tiles);
            pq.add(initialBoard);
            List<Board> neighbors = getNeighbor(initialBoard);
            Iterator<Board> it = neighbors.listIterator();
            while (it.hasNext()) {
                pq.add(it.next());
            }
            Board minPriority = pq.remove();
            while (!pq.isEmpty() && minPriority.manhattan() != 0) {
                minPriority = pq.remove();
                neighbors = getNeighbor(minPriority);
                it = neighbors.listIterator();
                while (it.hasNext()) {
                    pq.add(it.next());
                }
            }
            Stack<Board> path = new Stack<Board>();
            Board pathPiece = minPriority;
            System.out.println(minPriority.moves());
            while (pathPiece != null) {
                path.push(pathPiece);
                pathPiece = pathPiece.parent();
            }
            while (!path.isEmpty()) {
                System.out.println(path.pop());
            }
        }else{
            System.out.println("Unsolvable");
        }
    }

    public static boolean isSolvable(int[][]tiles) {
        int dim = tiles.length;
        int[] rmo = new int[(dim*dim)-1]; //row major order array
        for(int r = 0, i=0; r < dim; r++){
            for(int c = 0; c< dim; c++){
                if(tiles[r][c]!=0){
                    rmo[i++]= tiles[r][c];
                }
            }
        }
        boolean solvable = true;
        if(dim%2==0){ //if even
            int inv = calcInversions(rmo);
            int[]blankPos = blankPosition(tiles);
            if((blankPos[0]+inv)%2==0)solvable=false;
        }else{ //if odd
            int inv = calcInversions(rmo);
            if(inv%2!=0)solvable=false;
        }
        
        return solvable;
    }

    public static int calcInversions(int[]x){
        int inversions = 0;
        int dim = x.length;
        for(int i = 0; i<dim;i++){
            for(int j = i; j<dim;j++){
                if(x[i]>x[j])inversions++;
            }
        }
        return inversions;
    }

    public static int[] blankPosition(int[][]tiles){
        int[] blankPos = { -1, -1 };
        boolean foundBlank = false;
        int dim = tiles.length;
        for (int row = 0; row < dim && !foundBlank; row++) {
            for (int col = 0; col < dim && !foundBlank; col++) {
                foundBlank = tiles[row][col] == 0;
                if (foundBlank) {
                    blankPos[0] = row;
                    blankPos[1] = col;
                }
            }
        }
        return blankPos;
    }

    public static List<Board> getNeighbor(Board board) {
        List<Board> b = new ArrayList<Board>();
        int dim = board.dimension();
        int[][] tiles = board.tiles();
        int[]blankPos= blankPosition(tiles);
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

}
