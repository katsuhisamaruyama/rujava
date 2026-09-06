
import java.util.List;
import java.util.ArrayList;

import org.jtool.rujava.annotation.Mutable;
import org.jtool.rujava.annotation.Immutable;
import org.jtool.rujava.annotation.Owned;
import org.jtool.rujava.annotation.Borrow;

public class Example127 {
    
    Example127() {
    }
    
    void exec() {
        @Mutable @Owned List<Integer> muList = new ArrayList<>(List.of(1, 2));
        
        @Immutable @Borrow List<Integer> muList2 = muList;
        @Immutable @Borrow List<Integer> muList3 = muList;
        
        System.out.println(muList);
        
        System.out.println(muList2);
        
        System.out.println(muList3);
        
        @Mutable @Borrow List<Integer> muList4 = muList;    // Error "AssignBorrowed"
        
        System.out.println(muList4);
        
        System.out.println(muList2);
        
        System.out.println(muList3);
    }
    
    public static void main(String[] args) {
        Example127 example = new Example127();
        example.exec();
    }
}
