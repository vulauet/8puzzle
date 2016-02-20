import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.In;

public class Board {
    // construct a board from an N-by-N array of blocks (where blocks[i][j] = block in row i, column j)
    private int[] board;
    private int size;
    private int sizeSquare;

    public Board(int[][] blocks) {
        size = blocks.length;
        sizeSquare = size*size;
        board = new int[sizeSquare];
        for (int i = 0; i < size; i++)
            for (int j=0; j < size; j++) 
                board[i*size + j] = blocks[i][j];                
    }         
    
    private Board makeBoard(int[] blocks) {
        int[][] newBoard = new int[size][size];
        for (int i=0; i<size; i++)
            for (int j=0; j<size; j++)
                newBoard[i][j] = blocks[i*size + j];
        return new Board(newBoard);
    }

    // board dimension N
    public int dimension() { return size; }                

    // number of blocks out of place
    public int hamming() { 
        int score = 0;
        for (int i=0; i<sizeSquare; i++) if (board[i] != i && board[i] != 0) score++;
        return score;
    }                  

    // sum of Manhattan distances between blocks and goal
    public int manhattan() {
        int score = 0;
        for (int i=0; i<sizeSquare; i++) if (board[i] != 0) score += Math.abs(board[i]/size - i/size) + Math.abs(board[i]%size - i%size);
        return score;
    }                

    // is this board the goal board?
    public boolean isGoal() {
        for (int i=0; i<sizeSquare; i++) if (board[i] != i+1) return false;
        return true;
    }               

    // a board that is obtained by exchanging any pair of blocks
    public Board twin() {
        int[][] blocks = new int[size][size];
        int index = findZero();
        for (int i = 0; i<size; i++) {
            for (int j=0; j<size; j++ ) {
                blocks[i][j] = board[i*size + j];
                if (board[i*size + j] == 0) index = i*size + j;
            }
        }
        int i = 0;
        while (i == index || i+1 == index || (i+1)/size - i/size > 0) i++; 
        int tmp = blocks[i/size][i%size];
        blocks[i/size][i%size] = blocks[i/size + 1][i%size];
        blocks[i/size + 1][i%size] = tmp;
        return new Board(blocks);
    }                   

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        Board other = (Board) y;
        for (int i=0; i<sizeSquare; i++) if (board[i] != other.board[i]) return false;
        return true; 
    }       

    // all neighboring boards
    public Iterable<Board> neighbors() {
        Stack<Board> nb = new Stack<Board>();
        int zeroIndex = findZero();
        int[] tmpBoard = new int[sizeSquare];
        copyBoard(board, tmpBoard);
        int i = zeroIndex/size;
        int j = zeroIndex%size;
        if (valid(i, j+1)) nb.push(makeBoard(swap(board, zeroIndex, zeroIndex+1)));
        if (valid(i, j-1)) nb.push(makeBoard(swap(board, zeroIndex, zeroIndex-1)));
        if (valid(i+1, j)) nb.push(makeBoard(swap(board, zeroIndex, zeroIndex+size)));
        if (valid(i-1, j)) nb.push(makeBoard(swap(board, zeroIndex, zeroIndex-size)));
        return nb;
    }    

    private int findZero() { 
        int index = 0;
        for (int i = 0; i<sizeSquare; i++) if (board[i] == 0) index = i; 
        return index;
    }

    private void copyBoard(int[] src, int[] des) { for (int i = 0; i<sizeSquare; i++) des[i] = src[i]; }

    private boolean valid(int x, int y) { return x >= 0 && x < size && y >= 0 && y < size; }

    private int[] swap(int[] orig, int index1, int index2) {
        int[] newBoard = new int[sizeSquare];
        for (int i=0; i<sizeSquare; i++) newBoard[i] = orig[i];
        int tmp = orig[index1];
        orig[index1] = orig[index2];
        orig[index2] = tmp;
        return newBoard;
    }

    // string representation of this board (in the output format specified below)
    public String toString() {
        String result = size + "\n";
        for (int i=0; i<size; i++ ) {
            for (int j=0; j<size ; j++ ) {
                result = result + board[i*size + j] + " ";
            }
            result = result + "\n";
        }
        return result;
    }             

    // unit tests (not graded)
    public static void main(String[] args) {

    }
}