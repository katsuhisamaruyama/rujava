
import java.util.List;
import java.util.ArrayList;

import org.jtool.rujava.annotation.Mutable;
import org.jtool.rujava.annotation.Immutable;
import org.jtool.rujava.annotation.Owned;
import org.jtool.rujava.annotation.Borrow;

public class Example115 {
    
    Example115() {
    }
    
    void exec() {
        @Mutable @Owned List<Integer> muList = new ArrayList<>(List.of(1, 2));
        
        System.out.println(muList);
        
        @Mutable @Owned List<Integer> muList2 = muList;
        
        System.out.println(muList2);
        
        muList = muList;    // Error "AccessMoved"
    }
    
    public static void main(String[] args) {
        Example115 example = new Example115();
        example.exec();
    }
}
