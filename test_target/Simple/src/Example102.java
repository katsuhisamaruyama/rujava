
import java.util.List;
import java.util.ArrayList;

import org.jtool.rujava.annotation.Mutable;
import org.jtool.rujava.annotation.Immutable;
import org.jtool.rujava.annotation.Owned;
import org.jtool.rujava.annotation.Borrow;

public class Example102 {
    
    Example102() {
    }
    
    void exec() {
        @Mutable @Owned List<Integer> muList = new ArrayList<>(List.of(1, 2));
        
        System.out.println(muList);
        
        muList.add(3);
        
        System.out.println(muList);
    }
    
    public static void main(String[] args) {
        Example102 example = new Example102();
        example.exec();
    }
}
