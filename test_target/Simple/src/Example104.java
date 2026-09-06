
import java.util.List;
import java.util.ArrayList;

import org.jtool.rujava.annotation.Mutable;
import org.jtool.rujava.annotation.Immutable;
import org.jtool.rujava.annotation.Owned;
import org.jtool.rujava.annotation.Borrow;

public class Example104 {
    
    Example104() {
    }
    
    void exec() {
        @Immutable @Owned List<Integer> imList = new ArrayList<>(List.of(1, 2));
        @Mutable @Owned List<Integer> muList = new ArrayList<>(List.of(1, 2));
        
        System.out.println(imList);
        System.out.println(muList);
        
        imList.add(3);    // Error "ImmutableRedefine"
        muList.add(3);
        
        System.out.println(imList);
        System.out.println(muList);
    }
    
    public static void main(String[] args) {
        Example104 example = new Example104();
        example.exec();
    }
}
