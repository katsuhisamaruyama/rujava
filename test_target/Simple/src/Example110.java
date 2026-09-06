
import java.util.List;
import java.util.ArrayList;

import org.jtool.rujava.annotation.Mutable;
import org.jtool.rujava.annotation.Immutable;
import org.jtool.rujava.annotation.Owned;
import org.jtool.rujava.annotation.Borrow;

public class Example110 {
    
    Example110() {
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
    
    void m1(@Mutable @Owned List<Integer> p) {
    }
    
    void m2(@Immutable @Owned List<Integer> p) {
    }
    
    void m3(@Mutable @Borrow List<Integer> p) {
    }
    
    void m4(@Immutable @Borrow List<Integer> p) {
    }
    
    void exec() {
        m1(createMutableList());
        m2(createMutableList());
        m3(createMutableList());
        m4(createMutableList());
        
        m1(createImmutableList());    // Error "ImmutableAsMutable" in the strict mode
        m2(createImmutableList());
        m3(createImmutableList());    // Error "BorrowAsMutable"
        m4(createImmutableList());
        
        m1(createMutableList2());    // Error "OwnBorrowed"
        m2(createMutableList2());    // Error "OwnBorrowed"
        m3(createMutableList2());    // Error "MultipleMutableBorrowed" in the strict mode
        m4(createMutableList2());
        
        m1(createImmutableList2());    // Error "OwnBorrowed"
        m2(createImmutableList2());    // Error "OwnBorrowed"
        m3(createImmutableList2());    // Error "BorrowAsMutable"
        m4(createImmutableList2());
    }
    
    public static void main(String[] args) {
        Example110 example = new Example110();
        example.exec();
    }
}
