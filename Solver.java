import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.MinPQ;

public class Solver {
	private int numOfMove;
	private boolean solvable;
	private MinPQ<Node> openSet;
	private MinPQ<Node> twinSet;
	private Node start;
	private Node last;
	private Node evilTwin;
	private Stack<Board> step;
	
	private class Node implements Comparable<Node> {
		int step = 0;
		Board board;
		Node prev;
		int fScore;
		
		Node(Board b, int stepMade, Node prevNode) {
			board = b;
			step = stepMade;
			prev = prevNode;
			fScore = step + b.manhattan();
		}
		
		public int compareTo(Node other) {
			if (fScore < other.fScore) return -1;
			else if (fScore > other.fScore) return 1;
			return 0;
		}
	}
	
    public Solver(Board initial)           // find a solution to the initial board (using the A* algorithm)
    {
		openSet = new MinPQ<Node>();
		twinSet = new MinPQ<Node>();
		solvable = false;
		numOfMove = -1;
		start = new Node(initial, 0, null);
		evilTwin = new Node(start.board.twin(), 0, null);
		openSet.insert(start);
		twinSet.insert(evilTwin);
		Node current;
		Node newNeighbor;
		Node alternateCurrent;
		
		while (!openSet.isEmpty() || !twinSet.isEmpty()) {
			current = openSet.delMin();
			alternateCurrent = twinSet.delMin();
			
			if (current.board.isGoal()) {
				solvable = true;
				last = current;
				break;
			}
			
			if (alternateCurrent.board.isGoal()) {
				break;
			}
			
			for (Board b : current.board.neighbors()) {
				if (current.prev != null && b.equals(current.prev.board)) continue;
				newNeighbor = new Node(b, current.step + 1, current);
				openSet.insert(newNeighbor);
			}
			
			for (Board b : alternateCurrent.board.neighbors()) {
				if (alternateCurrent.prev != null && b.equals(alternateCurrent.prev.board)) continue;
				newNeighbor = new Node(b, alternateCurrent.step + 1, alternateCurrent);
				twinSet.insert(newNeighbor);
			}
		}
		
		if (solvable) {
			step = new Stack<Board>();
			Node cur = last;
			while (cur != null) {
				numOfMove++;
				step.push(cur.board);
				cur = cur.prev;
			}
		}
    }
    
    public boolean isSolvable()            // is the initial board solvable?
    {
    	return solvable;
    }

    public int moves()                     // min number of moves to solve initial board; -1 if unsolvable
    {
    	// if (numOfMove == 0) return -1;
		return numOfMove;
    }
    
    public Iterable<Board> solution()      // sequence of boards in a shortest solution; null if unsolvable
    {
		if (!isSolvable()) return null;
    	return step;
    }

    public static void main(String[] args) // solve a slider puzzle (given below)
    {
    	// create initial board from file
	    In in = new In(args[0]);
	    int N = in.readInt();
	    int[][] blocks = new int[N][N];
	    for (int i = 0; i < N; i++)
	        for (int j = 0; j < N; j++)
	            blocks[i][j] = in.readInt();
	    Board initial = new Board(blocks);

	    // solve the puzzle
	    Solver solver = new Solver(initial);

	    // print solution to standard output
	    if (!solver.isSolvable())
	        StdOut.println("No solution possible");
	    else {
	        StdOut.println("Minimum number of moves = " + solver.moves());
	        for (Board board : solver.solution())
	            StdOut.println(board);
	    }
	}
}