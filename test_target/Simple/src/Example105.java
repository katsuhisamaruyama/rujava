
import java.util.List;
import java.util.ArrayList;

import org.jtool.rujava.annotation.Mutable;
import org.jtool.rujava.annotation.Immutable;
import org.jtool.rujava.annotation.Owned;
import org.jtool.rujava.annotation.Borrow;

public class Example105 {
    
    Example105() {
    }
    
    @Mutable @Owned List<Integer> createMutableList() {
        return new ArrayList<>(List.of(1, 2));
    }
    
    @Immutable @Owned List<Integer> createImmutableList() {
        return new ArrayList<>(List.of(1, 2));
    }
    
    @Mutable @Borrow List<Integer> createMutableList2() {
        return new ArrayList<>(List.of(1, 2));
    }
    
    @Immutable @Borrow List<Integer> createImmutableList2() {
        return new ArrayList<>(List.of(1, 2));
    }
    
    void exec() {
        @Mutable   @Owned  List<Integer> list11 = createMutableList();;
        @Immutable @Owned  List<Integer> list12 = createMutableList();;
        @Mutable   @Borrow List<Integer> list13 = createMutableList();;
        @Immutable @Borrow List<Integer> list14 = createMutableList();;
        
        @Mutable   @Owned  List<Integer> list21 = createImmutableList();    // Error "ImmutableAsMutable" in the strict mode
        @Immutable @Owned  List<Integer> list22 = createImmutableList();
        @Mutable   @Borrow List<Integer> list23 = createImmutableList();    // Error "BorrowAsMutable"
        @Immutable @Borrow List<Integer> list24 = createImmutableList();
        
        @Mutable   @Owned  List<Integer> list31 = createMutableList2();    // Error "OwnBorrowed"
        @Immutable @Owned  List<Integer> list32 = createMutableList2();    // Error "OwnBorrowed"
        @Mutable   @Borrow List<Integer> list33 = createMutableList2();    // Error "MultipleMutableBorrowed" in the strict mode
        @Immutable @Borrow List<Integer> list34 = createMutableList2();
        
        @Mutable   @Owned  List<Integer> list41 = createImmutableList2();    // Error "OwnBorrowed"
        @Immutable @Owned  List<Integer> list42 = createImmutableList2();    // Error "OwnBorrowed"
        @Mutable   @Borrow List<Integer> list43 = createImmutableList2();    // Error "BorrowAsMutable"
        @Immutable @Borrow List<Integer> list44 = createImmutableList2();
    }
    
    public static void main(String[] args) {
        Example105 example = new Example105();
        example.exec();
    }
}
