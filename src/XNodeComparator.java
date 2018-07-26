import java.util.Comparator;
public class XNodeComparator implements Comparator<Node> {
    public int compare(Node n1, Node n2) {

        if(n1.getX()>n2.getX()) return 1;

        else if(n1.getX()<n2.getX()) return -1;

        return 0;
    }
}
