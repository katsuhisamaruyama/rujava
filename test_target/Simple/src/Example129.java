
import java.util.List;
import java.util.ArrayList;

import org.jtool.rujava.annotation.Mutable;
import org.jtool.rujava.annotation.Immutable;
import org.jtool.rujava.annotation.Owned;
import org.jtool.rujava.annotation.Borrow;

public class Example129 {
    
    Example129() {
    }
    
    void exec() {
        @Mutable @Owned List<Integer> muList = new ArrayList<>(List.of(1, 2));
        
        @Mutable @Borrow List<Integer> muList2 = muList;
        @Mutable @Borrow List<Integer> muList3 = muList;    // Error "AssignMutableBorrowed"
        
        System.out.println(muList);
        
        System.out.println(muList2);
        
        System.out.println(muList3);
        
        @Immutable @Owned List<Integer> muList4 = muList;    // Error "AssignOwned"
        
        System.out.println(muList4);
        
        @Mutable @Borrow List<Integer> muList5 = muList;    // Error "AssignMutableBorrowed"
        
        System.out.println(muList5);
        
        for (@Mutable @Borrow Integer item : muList) {
            System.out.println(item);
        }
    }
    
    public static void main(String[] args) {
        Example129 example = new Example129();
        example.exec();
    }
}
