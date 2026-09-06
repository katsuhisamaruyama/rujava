
import java.util.List;
import java.util.ArrayList;

import org.jtool.rujava.annotation.Mutable;
import org.jtool.rujava.annotation.Immutable;
import org.jtool.rujava.annotation.Owned;
import org.jtool.rujava.annotation.Borrow;

public class Example121 {
    
    Example121() {
    }
    
    @Mutable @Borrow List<Integer> createMutableList(@Immutable @Borrow List<Integer> list) {
        return new ArrayList<>(list);
    }
    
    void exec() {
        @Mutable @Borrow List<Integer> outList;
        
        {
            @Immutable @Owned List<Integer> imList = List.of(1, 2);
            
            System.out.println(imList);
            
            outList = createMutableList(imList);
            
            System.out.println(outList);
        }
        
        System.out.println(outList);    // Error "AccessBorrowed"
    }
    
    public static void main(String[] args) {
        Example121 example = new Example121();
        example.exec();
    }
}
