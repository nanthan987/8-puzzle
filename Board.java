import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.LinkedList;

public class Board {

    private final int[][] board;
    private final int n; // dimension
    private int[] blankPosition; // position of blank (0) tile


    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        n = tiles.length;
        this.board = copy(tiles);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (board[i][j] == 0) blankPosition = new int[]{i, j};
            }
        }
    }

    // string representation of this board
    public String toString() {
        StringBuilder boardString = new StringBuilder();
        boardString.append(this.dimension()).append("\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                boardString.append(board[i][j]);
                if (j == n - 1) break;
                boardString.append(" ");
            }
            boardString.append("\n");
        }
        return boardString.toString();
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // number of tiles out of place
    public int hamming() {
        int hammingDistance = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (board[i][j] != (n * i + (j + 1)) && board[i][j] != 0) hammingDistance++;
            }
        }
        return hammingDistance;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int manhattanDistance = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (board[i][j] != 0 && board[i][j] != (n * i + (j + 1))) {
                    int yGoal = Math.floorDiv(board[i][j] - 1, n);
                    int xGoal = board[i][j] - (n * yGoal) - 1;
                    manhattanDistance += Math.abs(i - yGoal) + Math.abs(j - xGoal);
                }
            }
        }
        return manhattanDistance;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return this.manhattan() == 0;
    }


    // does this board equal y?
    public boolean equals(Object y) {
        if (y == this) {
            return true;
        }

        if (!(y instanceof Board)) {
            return false;
        }

        if (n != ((Board) y).dimension()) return false;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (board[i][j] != ((Board) y).board[i][j]) return false;
            }
        }
        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        LinkedList<Board> neighbors = new LinkedList<Board>();

        // neighbor - up
        if (blankPosition[0] != 0) {
            neighbors.add(swap(blankPosition[0], blankPosition[1], blankPosition[0] - 1, blankPosition[1]));
        }

        // neighbor down
        if (blankPosition[0] != n - 1) {
            neighbors.add(swap(blankPosition[0], blankPosition[1], blankPosition[0] + 1, blankPosition[1]));
        }

        // neighbor left
        if (blankPosition[1] != 0) {
            neighbors.add(swap(blankPosition[0], blankPosition[1], blankPosition[0], blankPosition[1] - 1));
        }

        // neighbor right
        if (blankPosition[1] != n - 1) {
            neighbors.add(swap(blankPosition[0], blankPosition[1], blankPosition[0], blankPosition[1] + 1));
        }

        return neighbors;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        if (board[0][0] != 0 && board[0][1] != 0) {
            return swap(0, 0, 0, 1);
        } else return swap(1, 0, 1, 1);

    }

    // swap specified tiles in the instance's board
    private Board swap(int x1, int y1, int x2, int y2) {
        int[][] clone = copy(board);

        int temp = clone[x2][y2];
        clone[x2][y2] = clone[x1][y1];
        clone[x1][y1] = temp;
        return new Board(clone);
    }

    // copy of 2d array
    private int[][] copy(int[][] that) {
        int[][] copy = new int[n][n];
        for (int i = 0; i < n; i++) {
            copy[i] = that[i].clone();
        }
        return copy;
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);
        StdOut.println("Neighbors: ");
        for (Board temp : initial.neighbors()) {
            StdOut.println(temp);
        }

        StdOut.println("The Board: ");
        StdOut.println(initial);

        StdOut.println("Hamming Distance: ");
        StdOut.println(initial.hamming());

        StdOut.println("Manhattan Distance: ");
        StdOut.println(initial.manhattan());

        StdOut.println("Twin: ");
        StdOut.println(initial.twin());
    }

}
