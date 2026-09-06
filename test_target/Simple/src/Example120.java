
import java.util.List;
import java.util.ArrayList;

import org.jtool.rujava.annotation.Mutable;
import org.jtool.rujava.annotation.Immutable;
import org.jtool.rujava.annotation.Owned;
import org.jtool.rujava.annotation.Borrow;

public class Example120 {
    
    Example120() {
    }
    
    void exec() {
        @Mutable @Borrow List<Integer> outList;
        
        {
            @Mutable @Owned List<Integer> muList = new ArrayList<>(List.of(1, 2));
            
            System.out.println(muList);
            
            outList = muList;
            
            System.out.println(outList);
        }
        
        System.out.println(outList);    // Error "AccessBorrowed"
    }
    
    public static void main(String[] args) {
        Example120 example = new Example120();
        example.exec();
    }
}
