
import java.util.List;
import java.util.ArrayList;

import org.jtool.rujava.annotation.Mutable;
import org.jtool.rujava.annotation.Immutable;
import org.jtool.rujava.annotation.Owned;
import org.jtool.rujava.annotation.Borrow;

public class Example109 {
    
    Example109() {
    }
    
    void exec() {
        @Mutable @Owned List<Integer> muList = new ArrayList<>(List.of(1, 2));
        
        @Immutable @Owned List<Integer> imList = muList;
        
        @Mutable @Owned List<Integer> muList2 = imList;    // Error "ImmutableAsMutable" in the strict mode
        
        @Mutable @Borrow List<Integer> muList3 = new ArrayList<>(List.of(1, 2));
        
        @Mutable @Borrow List<Integer> muList4 = muList3;  // Error "MultipleMutableBorrowed" in the strict mode
    }
    
    public static void main(String[] args) {
        Example109 example = new Example109();
        example.exec();
    }
}
