
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import org.jtool.rujava.annotation.Mutable;
import org.jtool.rujava.annotation.Immutable;
import org.jtool.rujava.annotation.Owned;
import org.jtool.rujava.annotation.Borrow;

public class Example133 {
    
    Example133() {
    }
    
    void print(@Mutable @Owned List<Integer> muList) {
        for (@Mutable @Borrow int i = 0; i < muList.size(); i++) {
            System.out.println(i);
        }
    }
    
    void exec() {
        @Mutable @Owned List<Integer> muList = new ArrayList<>(List.of(1, 2));
        
        for (@Mutable @Borrow Iterator it = muList.iterator(); it.hasNext(); ) {
            System.out.println(it.next());
        }
        
        print(muList);
    }
    
    public static void main(String[] args) {
        Example133 example = new Example133();
        example.exec();
    }
}
