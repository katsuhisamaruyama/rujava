
import java.util.List;
import java.util.ArrayList;

import org.jtool.rujava.annotation.Mutable;
import org.jtool.rujava.annotation.Immutable;
import org.jtool.rujava.annotation.Owned;
import org.jtool.rujava.annotation.Borrow;

public class Example106 {
    
    Example106() {
    }
    
    void print(@Immutable @Borrow List<Integer> list) {
        System.out.println(list);
    }
    
    void add(@Mutable @Borrow List<Integer> list, int item) {
        list.add(item);
    }
    
    @Mutable List<Integer> inverse(@Mutable List<Integer> list, int item) {
        List<Integer> reversedList = new ArrayList<>(list.reversed());
        return reversedList;
    }
    
    void exec() {
        @Immutable @Owned List<Integer> imList = new ArrayList<>(List.of(1, 2));
        
        @Mutable @Owned List<Integer> muList = new ArrayList<>(List.of(1, 2));
        
        print(imList);
        print(muList);
        
        add(imList, 3);    // Error "BorrowAsMutable"
        add(muList, 3);
        
        @Mutable List<Integer> list1 = inverse(imList, 3);    // Error "BorrowAsMutable"
        @Mutable List<Integer> list2 = inverse(muList, 3);
    }
    
    public static void main(String[] args) {
        Example106 example = new Example106();
        example.exec();
    }
}
