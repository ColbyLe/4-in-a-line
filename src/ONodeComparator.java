import java.util.Comparator;
public class ONodeComparator implements Comparator<Node> {
    public int compare(Node n1, Node n2) {

        if(n1.getO()>n2.getO()) return 1;

        else if(n1.getO()<n2.getO()) return -1;

        return 0;
    }
}
