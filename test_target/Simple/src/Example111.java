
import java.util.List;
import java.util.ArrayList;

import org.jtool.rujava.annotation.Mutable;
import org.jtool.rujava.annotation.Immutable;
import org.jtool.rujava.annotation.Owned;
import org.jtool.rujava.annotation.Borrow;

public class Example111 {
    
    Example111() {
    }
    
    void exec() {
        @Mutable @Owned List<Integer> muList = new ArrayList<>(List.of(1, 2));
        
        System.out.println(muList);
        
        @Mutable @Owned List<Integer> muList2 = muList;
        
        System.out.println(muList2);
        
        System.out.println(muList);    // Error "AccessMoved"
        
        @Immutable @Owned List<Integer> muList3 = muList;    // Error "AccessMoved"
         
        muList = new ArrayList<>(List.of(1, 2, 3));
        
        System.out.println(muList);
        
        muList = new ArrayList<>(List.of(1, 2, 3, 4));
        
        System.out.println(muList);
        
        @Mutable @Owned List<Integer> muList4 = muList;
        
        System.out.println(muList);   // Error "AccessMoved"
        
        muList = new ArrayList<>(List.of(1, 2, 3, 4, 5));
        
        System.out.println(muList);
    }
    
    public static void main(String[] args) {
        Example111 example = new Example111();
        example.exec();
    }
}
