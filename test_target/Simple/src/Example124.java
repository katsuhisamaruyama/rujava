
import java.util.List;
import java.util.ArrayList;

import org.jtool.rujava.annotation.Mutable;
import org.jtool.rujava.annotation.Immutable;
import org.jtool.rujava.annotation.Owned;
import org.jtool.rujava.annotation.Borrow;

public class Example124 {
    
    Example124() {
    }
    
    void exec() {
        @Mutable @Owned List<Integer> muList = new ArrayList<>(List.of(1, 2));
        
        @Immutable @Borrow List<Integer> muList2 = muList;
        @Immutable @Borrow List<Integer> muList3 = muList;
        
        System.out.println(muList);
        
        System.out.println(muList2);
        
        System.out.println(muList3);
        
        @Immutable @Owned List<Integer> muList4 = muList;
        
        System.out.println(muList4);
    }
    
    public static void main(String[] args) {
        Example124 example = new Example124();
        example.exec();
    }
}
