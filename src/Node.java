import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Random;

public class Node {
    private int[][] board;
    private int empty, depth, xEval, oEval;

    public Node() {
        board = new int[8][8];
        empty = 64;
        depth = 0;
        xEval = 0;
        oEval = 0;
    }

    public Node(int[][] in, int e, int d) {
        board = in;
        empty = e;
        depth = d;

        int x = 0, o = 0, buf = 0;
        boolean iCont, jCont;
        int[][] b = this.board;

        // get non-terminal evaluation values for Node
        // check for number of rows of matching elements that are equal to search depth
        for(int i=0; i<b.length; i++) {
            for(int j=0; j<b[i].length-this.depth; j++) {
                buf = b[i][j];
                jCont = true;
                for(int k=1; k<this.depth; k++) {
                    if(buf == b[i][j+k]) continue;
                    else jCont = false;
                }

                if(jCont == true) {
                    if(buf == 1) x++;
                    if(buf == -1) o++;
                }
            }
        }

        // check for number of columns of matching elements that are equal to search depth
        for(int i=0; i<b.length-this.depth; i++) {
            for(int j=0; j<b.length; j++) {
                buf = b[i][j];
                iCont = true;
                for(int k=1; k<this.depth; k++) {
                    if(buf == b[i+k][j]) continue;
                    else iCont = false;
                }

                if(iCont == true) {
                    if(buf == 1) x++;
                    if(buf == -1) o++;
                }
            }
        }
        xEval = x;
        oEval = o;

        int win = 0;
        for(int i=0; i<board.length; i++) {
            for(int j=0; j<board[i].length; j++) {
                // if not empty, check for 4 in a line
                if(board[i][j]!=0) {
                    if(check4(i, j, board[i][j])) win = board[i][j];
                }
            }
        }

        if(win == -1) {
            oEval = Integer.MAX_VALUE;
            xEval = Integer.MIN_VALUE;
        }

        if(win == 1) {
            oEval = Integer.MIN_VALUE;
            xEval = Integer.MAX_VALUE;
        }

    }

    // method to process human player moves
    // return false if spot is taken
    // return true if move is successful
    public boolean move(String in) {
        int x = Character.toLowerCase(in.charAt(0)) - 97;
        int y = in.charAt(1) - 49;

        // check if space is taken (check for valid input in driver
        if(board[x][y]!=0) return false;

        empty--;
        depth++;
        board[x][y] = 1;

        xEval = this.getX();
        oEval = this.getO();

        return true;
    }

    //generate list of possible next moves sorted by X evaluation value
    public static PriorityQueue<Node> getAIMoveX(Node in) {
        // ArrayList<Node> next = new ArrayList<>();
        PriorityQueue<Node> next = new PriorityQueue<Node>(new XNodeComparator());
        int[][] clone;

        if(in.getEmpty() == 64) {
            Random rd = new Random();
            int i = rd.nextInt(8);
            int j = rd.nextInt(8);
            clone = in.copyBoard();
            clone[i][j] = -1;
            next.add(new Node(clone, in.empty-1, (in.depth+1)));
        }

        //ArrayList<String> occ = new ArrayList<>();
        // for each occupied space, fill adjacent spaces
        for (int i = 0; i < in.board.length; i++) {
            for (int j = 0; j < in.board[i].length; j++) {
                // fill space adjacent to occupied spaces
                if (in.board[i][j] != 0) {
                    // add above
                    if (i > 0 && in.board[i - 1][j] == 0) {
                        clone = in.copyBoard();
                        clone[i - 1][j] = 1;
                        next.add(new Node(clone, in.empty-1,(in.depth+1)));
                    }

                    // add below
                    if (i < in.board.length - 1 && in.board[i + 1][j] == 0) {
                        clone = in.copyBoard();
                        clone[i + 1][j] = 1;
                        next.add(new Node(clone, in.empty-1,(in.depth+1)));
                    }

                    // add left
                    if (j > 0 && in.board[i][j - 1] == 0) {
                        clone = in.copyBoard();
                        clone[i][j - 1] = 1;
                        next.add(new Node(clone, in.empty-1,(in.depth+1)));
                    }

                    // add right
                    if (j < in.board.length - 1 && in.board[i][j + 1] == 0) {
                        clone = in.copyBoard();
                        clone[i][j + 1] = 1;
                        next.add(new Node(clone, in.empty-1,(in.depth+1)));
                    }

                    // add upper left
                    if (i > 0 && j > 0 && in.board[i - 1][j - 1] == 0) {
                        clone = in.copyBoard();
                        clone[i-1][j-1] = 1;
                        next.add(new Node(clone, in.empty-1,(in.depth+1)));
                    }

                        // add upper right
                    if (i > 0 && j < in.board.length - 1 && in.board[i - 1][j + 1] == 0) {
                        clone = in.copyBoard();
                        clone[i-1][j + 1] = 1;
                        next.add(new Node(clone, in.empty-1,(in.depth+1)));
                    }

                    // add lower right
                    if (i < in.board.length - 1 && j < in.board.length - 1 && in.board[i + 1][j + 1] == 0) {
                        clone = in.copyBoard();
                        clone[i+1][j + 1] = 1;
                        next.add(new Node(clone, in.empty-1,(in.depth+1)));
                    }

                    // add lower left
                    if (i < in.board.length - 1 && j > 0 && in.board[i + 1][j - 1] == 0) {
                        clone = in.copyBoard();
                        clone[i+1][j - 1] = 1;
                        next.add(new Node(clone, in.empty-1,(in.depth+1)));
                    }
                }
            }
        }
        return next;
    }

    // generate list of possible next moves sorted by O evaluation value
    public static PriorityQueue<Node> getAIMoveO(Node in) {
        //ArrayList<Node> next = new ArrayList<>();
        int[][] clone;
        PriorityQueue<Node> next = new PriorityQueue<Node>(new ONodeComparator());

        if(in.getEmpty() == 64) {
            Random rd = new Random();
            int i = rd.nextInt(8);
            int j = rd.nextInt(8);
            clone = in.copyBoard();
            clone[i][j] = -1;
            next.add(new Node(clone, in.empty-1, (in.depth+1)));
        }

        //ArrayList<String> occ = new ArrayList<>();
        // for each occupied space, fill adjacent spaces
        for (int i = 0; i < in.board.length; i++) {
            for (int j = 0; j < in.board[i].length; j++) {
                // fill space adjacent to occupied spaces
                if (in.board[i][j] != 0) {
                    // add above
                    if (i > 0 && in.board[i - 1][j] == 0) {
                        clone = in.copyBoard();
                        clone[i - 1][j] = -1;
                        next.add(new Node(clone, in.empty-1,(in.depth+1)));
                    }

                    // add below
                    if (i < in.board.length - 1 && in.board[i + 1][j] == 0) {
                        clone = in.copyBoard();
                        clone[i + 1][j] = -1;
                        next.add(new Node(clone, in.empty-1,(in.depth+1)));
                    }

                    // add left
                    if (j > 0 && in.board[i][j - 1] == 0) {
                        clone = in.copyBoard();
                        clone[i][j - 1] = -1;
                        next.add(new Node(clone, in.empty-1,(in.depth+1)));
                    }

                    // add right
                    if (j < in.board.length - 1 && in.board[i][j + 1] == 0) {
                        clone = in.copyBoard();
                        clone[i][j + 1] = -1;
                        next.add(new Node(clone, in.empty-1,(in.depth+1)));
                    }

                    // add upper left
                    if (i > 0 && j > 0 && in.board[i - 1][j - 1] == 0) {
                        clone = in.copyBoard();
                        clone[i-1][j-1] = -1;
                        next.add(new Node(clone, in.empty-1,(in.depth+1)));
                    }

                    // add upper right
                    if (i > 0 && j < in.board.length - 1 && in.board[i - 1][j + 1] == 0) {
                        clone = in.copyBoard();
                        clone[i-1][j + 1] = -1;
                        next.add(new Node(clone, in.empty-1,(in.depth+1)));
                    }

                    // add lower right
                    if (i < in.board.length - 1 && j < in.board.length - 1 && in.board[i + 1][j + 1] == 0) {
                        clone = in.copyBoard();
                        clone[i+1][j + 1] = -1;
                        next.add(new Node(clone, in.empty-1,(in.depth+1)));
                    }

                    // add lower left
                    if (i < in.board.length - 1 && j > 0 && in.board[i + 1][j - 1] == 0) {
                        clone = in.copyBoard();
                        clone[i+1][j - 1] = -1;
                        next.add(new Node(clone, in.empty-1,(in.depth+1)));
                    }
                }
            }
        }
        return next;
    }

    // method to check for 4 in a line
    // input: rows, columns, player symbol
    private boolean check4(int i, int j, int player) {
        boolean row=true, col=true;

        // check rows
        for(int x = j; x<j+4; x++) {
            if(x == 8) break;
            if(board[i][x] != player) row = false;
        }

        // check columns
        for(int x = i; x<i+4; x++) {
            if(x == 8) break;
            if(board[x][j] != player) col = false;
        }

        if (j + 4 <= board.length && row) return true;

        if (i + 4 <= board.length && col) return true;

        return false;
    }

    public int getWinner() {
        // for each item in board, check for 4 adjacent matching items
        for(int i=0; i<board.length; i++) {
            for(int j=0; j<board[i].length; j++) {
                // if not empty, check for 4 in a line
                if(board[i][j]!=0) {
                    if(check4(i, j, board[i][j])) return board[i][j];
                }
            }
        }
        // return -2 if tied
        if(empty == 0) return -2;

        // return 0 if no winner yet
        return 0;
    }

    public int getX() {
        return xEval;
    }

    public int getO() {
        return oEval;
    }

    public int getEmpty() {
        return empty;
    }

    /* public int getEval(int player) {
        int score = 0;
        int x = 0, o = 0, buf = 0;
        boolean iCont, jCont;
        int[][] b = this.board;

        // check for number of rows of matching elements that are equal to search depth
        for(int i=0; i<b.length; i++) {
            for(int j=0; j<b[i].length-this.depth; j++) {
                buf = b[i][j];
                jCont = true;
                for(int k=1; k<this.depth; k++) {
                    if(buf == b[i][j+k]) continue;
                    else jCont = false;
                }

                if(jCont == true) {
                    if(buf == 1) x++;
                    if(buf == -1) o++;
                }
            }
        }

        // check for number of columns of matching elements that are equal to search depth
        for(int i=0; i<b.length-this.depth; i++) {
            for(int j=0; j<b.length; j++) {
                buf = b[i][j];
                iCont = true;
                for(int k=1; k<this.depth; k++) {
                    if(buf == b[i+k][j]) continue;
                    else iCont = false;
                }

                if(iCont == true) {
                    if(buf == 1) x++;
                    if(buf == -1) o++;
                }
            }
        }

        if(player == 1) return x;
        if(player == -1) return o;
        return 0;
    } */

    public void printBoard() {
        char row = 'a';
        System.out.println("\n  1 2 3 4 5 6 7 8");
        for(int i=0; i<board.length; i++) {
            System.out.print("" + row++ + " ");
            for(int j=0; j<board.length; j++) {
                if(board[i][j] == 1) System.out.print("X ");
                else if(board[i][j] == -1) System.out.print("O ");
                else System.out.print("- ");
            }
            System.out.println();
        }
        System.out.println();
    }


    // utility function to copy board
    private int[][] copyBoard() {
        int[][] out = new int[board.length][board[0].length];
        for(int i=0; i< board.length; i++) {
            for(int j=0; j<board[i].length; j++) {
                out[i][j] = board[i][j];
            }
        }
        return out;
    }
}
