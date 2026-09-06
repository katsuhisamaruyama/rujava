
import java.util.List;
import java.util.ArrayList;

import org.jtool.rujava.annotation.Mutable;
import org.jtool.rujava.annotation.Immutable;
import org.jtool.rujava.annotation.Owned;
import org.jtool.rujava.annotation.Borrow;

public class Example132 {
    
    Example132() {
    }
    
    void print(@Mutable @Owned List<Integer> muList) {
        for (@Mutable @Borrow Integer i : muList) {
            System.out.println(i);
        }
    }
    
    void exec() {
        @Mutable @Owned List<Integer> muList = new ArrayList<>(List.of(1, 2));
        
        for (@Mutable @Borrow Integer i : muList) {
            System.out.println(i);
        }
        
        print(muList);
    }
    
    public static void main(String[] args) {
        Example132 example = new Example132();
        example.exec();
    }
}
