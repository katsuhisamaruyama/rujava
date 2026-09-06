
import java.util.List;
import java.util.ArrayList;

import org.jtool.rujava.annotation.Mutable;
import org.jtool.rujava.annotation.Immutable;
import org.jtool.rujava.annotation.Owned;
import org.jtool.rujava.annotation.Borrow;

public class Example128 {
    
    Example128() {
    }
    
    void exec() {
        @Mutable @Owned List<Integer> muList = new ArrayList<>(List.of(1, 2));
        
        @Mutable @Borrow List<Integer> muList2 = muList;
        
        System.out.println(muList);
        
        System.out.println(muList2);
        
        @Immutable @Owned List<Integer> muList3 = muList;    // Error "AssignOwned"
        
        System.out.println(muList3);
    }
    
    public static void main(String[] args) {
        Example128 example = new Example128();
        example.exec();
    }
}
