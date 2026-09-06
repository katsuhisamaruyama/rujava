
import java.util.List;
import java.util.ArrayList;

import org.jtool.rujava.annotation.Mutable;
import org.jtool.rujava.annotation.Immutable;
import org.jtool.rujava.annotation.Owned;
import org.jtool.rujava.annotation.Borrow;

public class Example119 {
    
    Example119() {
    }
    
    void print(@Mutable @Borrow List<Integer> muList) {
        for (Integer i : muList) {
            System.out.println(i);
        }
    }
    
    void exec() {
        @Mutable @Owned List<Integer> muList = new ArrayList<>(List.of(1, 2));
        
        print(muList);
        
        System.out.println(muList);
        
        muList = new ArrayList<>(List.of(1, 3));
        
        System.out.println(muList);
        
        @Mutable @Owned List<Integer> muList2 = muList;
        
        System.out.println(muList);    // Error "AccessMoved"
    }
    
    public static void main(String[] args) {
        Example119 example = new Example119();
        example.exec();
    }
}
