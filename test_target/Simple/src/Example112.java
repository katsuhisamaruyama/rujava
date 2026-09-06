
import java.util.List;
import java.util.ArrayList;

import org.jtool.rujava.annotation.Mutable;
import org.jtool.rujava.annotation.Immutable;
import org.jtool.rujava.annotation.Owned;
import org.jtool.rujava.annotation.Borrow;

public class Example112 {
    
    Example112() {
    }
    
    void exec() {
        @Mutable @Owned List<Integer> muList = new ArrayList<>(List.of(1, 2));
        
        System.out.println(muList);
        
        if ("A".equals("B")) {
            @Mutable @Owned List<Integer> muList2 = muList;
            
            System.out.println(muList2);
            
            System.out.println(muList);    // Error "AccessMoved"
        }
        
        System.out.println(muList);    // Error "AccessMoved"
        
        if ("B".equals("C")) {
            muList = new ArrayList<>(List.of(1, 2, 3));
            
            System.out.println(muList);
        }
        
        System.out.println(muList);    // Error "AccessMoved"
    }
    
    public static void main(String[] args) {
        Example112 example = new Example112();
        example.exec();
    }
}
