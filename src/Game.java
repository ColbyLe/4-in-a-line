import java.util.PriorityQueue;
import java.util.Scanner;

public class Game {
    public static void main(String[] args) {
        Scanner kb = new Scanner(System.in);
        // true for person, false for AI
        boolean player;
        Node game = new Node();
        System.out.print("Would you like to go first? (Y/N): ");
        if(kb.nextLine().equalsIgnoreCase("Y")) player = true;
        else player = false;
        System.out.println();
        game.printBoard();

        while(game.getEmpty()>0) {
            // get player move
            boolean valid = false;
            if(player) {
                while(valid = false) {
                    System.out.print("Enter move (ex: a6): ");
                    String m = kb.nextLine();
                    if(m.length()>2 || Character.toLowerCase(m.charAt(0)) > 'h' || Character.toLowerCase(m.charAt(0)) < 'a' || m.charAt(1) > '8' || m.charAt(1) < '1') {
                        System.out.println("\n Invalid move, please try again.");
                        continue;
                    }
                    else {
                        valid = true;
                        game.move(m);
                    }
                }
                game.printBoard();
                if(game.getWinner() == 1) {
                    System.out.println("You win!");
                    break;
                }
            }

            else {
                // get AI move
                game = aBSearch(game);
                game.printBoard();
                if(game.getWinner() == -1) {
                    System.out.println("You lose.");
                    break;
                }
            }
        }

    }

    private static Node aBSearch(Node n) {
        int v = maxValue(n, Integer.MIN_VALUE, Integer.MAX_VALUE, 0);
        PriorityQueue<Node> next = Node.getAIMoveO(n);
        Node s = next.poll();
        while(!next.isEmpty()) {
            if(s.getO() == v) return s;
            else s = next.poll();
        }
        next = Node.getAIMoveO(n);
        return next.poll();
    }

    // start with O
    private static int maxValue(Node n, int min, int max, int d) {
        if(n.getWinner() == -1) return Integer.MAX_VALUE;
        if(n.getWinner() == 1) return Integer.MIN_VALUE;
        d++;
        int v = Integer.MIN_VALUE;
        if (d==3) return v;
        PriorityQueue<Node> next = Node.getAIMoveX(n);
        while(!next.isEmpty()) {
            Node s = next.poll();
            v = Math.max(v, minValue(s, min, max, d));
            if(v>=max) return v;
            min = Math.max(min, v);
        }
        return v;
    }

    private static int minValue(Node n, int min, int max, int d) {
        if(n.getWinner() == 1) return Integer.MAX_VALUE;
        if(n.getWinner() == -1) return Integer.MIN_VALUE;
        d++;
        int v = Integer.MAX_VALUE;
        if (d==3) return v;
        PriorityQueue<Node> next = Node.getAIMoveO(n);
        while(!next.isEmpty()) {
            Node s = next.poll();
            v = Math.min(v, maxValue(s, min, max, d));
            if(v<=min) return v;
            max = Math.min(max, v);
        }
        return v;
    }
}

