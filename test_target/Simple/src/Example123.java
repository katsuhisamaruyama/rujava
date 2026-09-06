
import java.util.List;
import java.util.ArrayList;

import org.jtool.rujava.annotation.Mutable;
import org.jtool.rujava.annotation.Immutable;
import org.jtool.rujava.annotation.Owned;
import org.jtool.rujava.annotation.Borrow;

public class Example123 {
    
    Example123() {
    }
    
    @Mutable @Borrow List<Integer> createMutableList(@Immutable @Borrow List<Integer> list, int n) {
        return new ArrayList<>(list);
    }
    
    void exec() {
        @Mutable @Borrow List<Integer> outList;
        
        {
            @Immutable @Owned List<Integer> imList = List.of(1, 2);
            
            System.out.println(imList);
            
            outList = createMutableList(imList, imList.size());
            
            System.out.println(outList);
        }
        
        System.out.println(outList);
    }
    
    public static void main(String[] args) {
        Example123 example = new Example123();
        example.exec();
    }
}
