
import java.util.List;
import java.util.ArrayList;

import org.jtool.rujava.annotation.Mutable;
import org.jtool.rujava.annotation.Immutable;
import org.jtool.rujava.annotation.Owned;
import org.jtool.rujava.annotation.Borrow;

public class Example117 {
    
    Example117() {
    }
    
    void print(@Mutable @Owned List<Integer> muList) {
        for (Integer i : muList) {
            System.out.println(i);
        }
    }
    
    void exec() {
        @Mutable @Owned List<Integer> muList = new ArrayList<>(List.of(1, 2));
        
        @Mutable @Borrow List<Integer> muList2 = muList;
        
        System.out.println(muList);
    }
    
    public static void main(String[] args) {
        Example117 example = new Example117();
        example.exec();
    }
}
