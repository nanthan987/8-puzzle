import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.LinkedList;

public class Solver {

    private boolean isSolvable;
    private final LinkedList<Board> solutions;

    private final class Node implements Comparable<Node> {
        private final Board board;
        private final int moves;
        private final Node previous;
        private final int priority;

        public Node(Board board, Node previous) {
            this.board = board;
            this.previous = previous;
            if (previous != null) this.moves = previous.moves + 1;
            else this.moves = 0;
            this.priority = board.manhattan() + moves;

        }

        public int compareTo(Node that) {
            if (this.priority == that.priority) return 0;
            if (this.priority > that.priority) return 1;
            return -1;
        }


    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException();

        Node initialNode = new Node(initial, null);
        Node twinNode = new Node(initial.twin(), null);

        solutions = new LinkedList<>();  // list of boards that lead to solution (if solvable)

        MinPQ<Node> pq = new MinPQ<>();
        MinPQ<Node> twinpq = new MinPQ<>();

        pq.insert(initialNode);
        twinpq.insert(twinNode);

        while (true) {
            Node min = pq.delMin();
            Node twinMin = twinpq.delMin();

            if (min.board.isGoal()) {
                // puzzle can be solved
                isSolvable = true;
                setSolutions(min);
                break;
            }
            if (twinMin.board.isGoal()) {
                // puzzle is unsolvable
                break;
            }

            // solving the board and its twin in lockstep

            // original board:
            for (Board neighbor : min.board.neighbors()) {
                if (min.previous == null) {
                    Node neighborNode = new Node(neighbor, min);
                    pq.insert(neighborNode);
                    continue;
                }
                if (!neighbor.equals(min.previous.board)) {
                    Node neighborNode = new Node(neighbor, min);
                    pq.insert(neighborNode);
                }
            }

            // twin board:
            for (Board neighbor : twinMin.board.neighbors()) {
                if (min.previous == null) {
                    Node neighborNode = new Node(neighbor, twinMin);
                    twinpq.insert(neighborNode);
                    continue;
                }
                if (!neighbor.equals(twinMin.previous.board)) {
                    Node neighborNode = new Node(neighbor, twinMin);
                    twinpq.insert(neighborNode);

                }
            }


        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return isSolvable;
    }

    // min number of moves to solve initial board
    public int moves() {
        if (isSolvable()) return solutions.size() - 1;
        else return -1;
    }

    private void setSolutions(Node goal) {
        Node mostRecentCopy = goal;
        while (mostRecentCopy != null) {
            solutions.addFirst(mostRecentCopy.board);
            mostRecentCopy = mostRecentCopy.previous;
        }
    }

    // sequence of boards in a shortest solution
    public Iterable<Board> solution() {

        if (isSolvable()) {
            return solutions;
        } else return null;
    }

    // test client (see below)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

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
