import java.util.ArrayList;
public class Node {
    private int[][] board;
    private int empty;

    public Node(int[][] in, int e) {
        board = in;
        empty = e;
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
    public static ArrayList<Node> aiMove(Node in) {
        ArrayList<Node> next = new ArrayList<>();
        int[][] clone;
        //ArrayList<String> occ = new ArrayList<>();
        //get list of occupied spaces
        for(int i=0; i<in.board.length; i++) {
            for(int j=0; j<in.board[i].length; j++) {
                // fill space adjacent to filled spaces
                if(in.board[i][j] != 0) {
                    // add above
                    if(i>0 && in.board[i-1][j] == 0) {
                        clone = in.copyBoard();
                        clone[i-1][j] = 1;
                        next.add(new Node(clone, in.empty));
                    }

                    // add below
                    if(i<in.board.length-1 && in.board[i+1][j] == 0) {
                        clone = in.copyBoard();
                        clone[i+1][j] = 1;
                        next.add(new Node(clone, in.empty));
                    }

                    // add left
                    if(j>0 && in.board[i][j-1] == 0) {
                        clone = in.copyBoard();
                        clone[i][j-1] = 1;
                        next.add(new Node(clone, in.empty));
                    }

                    // add right
                    if(j<in.board.length-1 && in.board[i][j+1] == 0) {
                        clone = in.copyBoard();
                        clone[i][j+1] = 1;
                        next.add(new Node(clone, in.empty));
                    }
                }
            }
        }
        return next;
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
