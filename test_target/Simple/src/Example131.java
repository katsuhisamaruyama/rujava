
import java.util.List;
import java.util.ArrayList;

import org.jtool.rujava.annotation.Mutable;
import org.jtool.rujava.annotation.Immutable;
import org.jtool.rujava.annotation.Owned;
import org.jtool.rujava.annotation.Borrow;

public class Example131 {
    
    Example131() {
    }
    
    void exec() {
        @Mutable @Owned List<Integer> muList = new ArrayList<>(List.of(1, 2));
        
        @Mutable @Borrow List<Integer> muList2 = muList;
        
        System.out.println(muList);
        
        System.out.println(muList2);
        
        @Immutable @Borrow List<Integer> muList3 = muList2;    // Error "AssignBorrowed"
        
        System.out.println(muList2);
        
        System.out.println(muList3);
    }
    
    public static void main(String[] args) {
        Example131 example = new Example131();
        example.exec();
    }
}
