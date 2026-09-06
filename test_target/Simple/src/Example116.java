
import java.util.List;
import java.util.ArrayList;

import org.jtool.rujava.annotation.Mutable;
import org.jtool.rujava.annotation.Immutable;
import org.jtool.rujava.annotation.Owned;
import org.jtool.rujava.annotation.Borrow;

public class Example116 {
    
    Example116() {
    }
    
    void exec() {
        @Mutable @Owned List<Integer> l1 = new ArrayList<>(List.of(1, 2));
        @Mutable @Owned List<Integer> l2 = new ArrayList<>(List.of(3, 4));
        @Mutable @Owned List<List> muList = new ArrayList<>(List.of(l1, l2));
        
        System.out.println(muList);
        
        for (@Mutable @Owned List l : muList) {    // Error "AccessMoved"
            System.out.println(l);
        }
        
        System.out.println(muList);    // Error "AccessMoved"
    }
    
    public static void main(String[] args) {
        Example116 example = new Example116();
        example.exec();
    }
}
