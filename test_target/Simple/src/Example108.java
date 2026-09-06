
import java.util.List;
import java.util.ArrayList;

import org.jtool.rujava.annotation.Mutable;
import org.jtool.rujava.annotation.Immutable;
import org.jtool.rujava.annotation.Owned;
import org.jtool.rujava.annotation.Borrow;

public class Example108 {
    
    Example108() {
    }
    
    void exec() {
        @Immutable @Owned List<Integer> imList;
        
        imList = new ArrayList<>(List.of(1, 2));
        
        imList = new ArrayList<>(List.of(1, 2, 3));    // Error "ImmutableRedefine"
        
        System.out.println(imList);
    }
    
    public static void main(String[] args) {
        Example108 example = new Example108();
        example.exec();
    }
}
