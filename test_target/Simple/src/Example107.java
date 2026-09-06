
import java.util.List;
import java.util.ArrayList;

import org.jtool.rujava.annotation.Mutable;
import org.jtool.rujava.annotation.Immutable;
import org.jtool.rujava.annotation.Owned;
import org.jtool.rujava.annotation.Borrow;

public class Example107 {
    
    Example107() {
    }
    
    void exec() {
        @Immutable @Owned List<Integer> imList = new ArrayList<>(List.of(1, 2));
        
        imList = new ArrayList<>(List.of(1, 2, 3));    // Error "ImmutableRedefine"
        
        System.out.println(imList);
    }
    
    public static void main(String[] args) {
        Example107 example = new Example107();
        example.exec();
    }
}
