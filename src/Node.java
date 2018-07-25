import java.util.ArrayList;
public class Node {
    private int[][] board;
    private int empty;
    private int depth;

    public Node(int[][] in, int e, int d) {
        board = in;
        empty = e;
        depth = d;
    }

    // true if human player, false if ai
    public void move(String in, boolean player) {
        int x = Character.toLowerCase(in.charAt(0)) - 97;
        int y = in.charAt(1) - 49;

        // check if space is taken (check for valid input in driver
        if(board[x][y]!=0) return;

        empty--;
        if(player) board[x][y] = -1;
        else board[x][y] = 1;
    }

    //generate list of possible next moves
    public static ArrayList<Node> getAIMove(Node in) {
        ArrayList<Node> next = new ArrayList<>();
        int[][] clone;
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
                        next.add(new Node(clone, in.empty,(in.depth+1)));
                    }

                    // add below
                    if (i < in.board.length - 1 && in.board[i + 1][j] == 0) {
                        clone = in.copyBoard();
                        clone[i + 1][j] = 1;
                        next.add(new Node(clone, in.empty,(in.depth+1)));
                    }

                    // add left
                    if (j > 0 && in.board[i][j - 1] == 0) {
                        clone = in.copyBoard();
                        clone[i][j - 1] = 1;
                        next.add(new Node(clone, in.empty,(in.depth+1)));
                    }

                    // add right
                    if (j < in.board.length - 1 && in.board[i][j + 1] == 0) {
                        clone = in.copyBoard();
                        clone[i][j + 1] = 1;
                        next.add(new Node(clone, in.empty,(in.depth+1)));
                    }

                    // add upper left
                    if (i > 0 && j > 0 && in.board[i - 1][j - 1] == 0) {
                        clone = in.copyBoard();
                        clone[i-1][j-1] = 1;
                        next.add(new Node(clone, in.empty,(in.depth+1)));
                    }

                        // add upper right
                    if (i > 0 && j < in.board.length - 1 && in.board[i - 1][j + 1] == 0) {
                        clone = in.copyBoard();
                        clone[i-1][j + 1] = 1;
                        next.add(new Node(clone, in.empty,(in.depth+1)));
                    }

                    // add lower right
                    if (i < in.board.length - 1 && j < in.board.length - 1 && in.board[i + 1][j + 1] == 0) {
                        clone = in.copyBoard();
                        clone[i+1][j + 1] = 1;
                        next.add(new Node(clone, in.empty,(in.depth+1)));
                    }

                    // add lower left
                    if (i < in.board.length - 1 && j > 0 && in.board[i + 1][j - 1] == 0) {
                        clone = in.copyBoard();
                        clone[i+1][j - 1] = 1;
                        next.add(new Node(clone, in.empty,(in.depth+1)));
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
            if(board[i][x] != player) row = false;
        }

        // check columns
        for(int x = i; x<i+4; x++) {
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
        return 0;
    }

    public int getEval(int player) {
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
    }

    public void printBoard() {
        char row = 'a';
        System.out.println("  1 2 3 4 5 6 7 8");
        for(int i=0; i<board.length; i++) {
            System.out.print("" + row++ + " ");
            for(int j=0; j<board[i].length; j++) {
                if(board[i][j] == 1) System.out.print("X ");
                else if(board[i][j] == -1) System.out.print("O ");
                else System.out.print("- ");
            }
            System.out.println();
        }
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
