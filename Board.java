
public class Board {

    private Board parent;
    private int[][] tiles;
    private int moves;
    private static int dimension;

    public Board(int[][] tiles) {
        this.tiles = tiles;
        dimension = tiles.length;
        this.moves = 0;
        this.parent = null;
    }

    public Board(int[][]tiles, Board parent){
        this.tiles = tiles;
        this.parent = parent;
        this.moves = parent.moves+1;
    }

    public String toString() {
        //questo toString è leggermente più complicato per fare in modo di ottenere
        //una stringa che non abbia spazi alla fine
        String s = "";
        for(int row = 0 ; row<dimension-1;row++){
            for(int col = 0; col< dimension; col++){
                s+=tiles[row][col]+" ";
            }
        }
        for(int col = 0; col<dimension-1;col++){
            s+=tiles[dimension-1][col]+" ";
        }
        s+=tiles[dimension-1][dimension-1]+"\n";
        return s;
    }

    

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int man = 0;
        for (int row = 0; row < dimension; row++) {
            for (int col = 0; col < dimension; col++) {
                int actualTile = tiles[row][col];
                if (actualTile != 0) {
                    int rightColumn = (actualTile - 1) % dimension;
                    int rightRow = (actualTile - 1) / dimension;
                    man+=(int)((Math.abs(rightColumn-col))+(Math.abs(rightRow-row)));
                }
            }
        }
        return man;
    }

    public int moves(){
        return moves;
    }

    public int dimension(){
        return dimension;
    }

    public int[][]tiles(){
        return tiles;
    }

    public boolean notEqualsAncient(){
        boolean found = false;
        Board ancient = parent;
        while(ancient!=null && !found){
            boolean eq = true;
            int[][]ancTiles = ancient.tiles();
            for(int row = 0; row<dimension && eq;row++){
                for(int col = 0; col<dimension && eq;col++){
                    eq = ancTiles[row][col] == tiles[row][col];
                }
            }
            found = eq;
            ancient = ancient.parent;
        }
        return !found;
    }

    public Board parent(){
        return parent;
    }
    
}