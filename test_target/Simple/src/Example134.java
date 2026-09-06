
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import org.jtool.rujava.annotation.Mutable;
import org.jtool.rujava.annotation.Immutable;
import org.jtool.rujava.annotation.Owned;
import org.jtool.rujava.annotation.Borrow;

public class Example134 {
    
    Example134() {
    }
    
    void print(List<Integer> list) {
        for (Integer item : list) {
            System.out.println(item);
        }
    }
    
    void exec() {
        List<Integer> imuList = new ArrayList<>(List.of(1, 2));
        
        print(imuList);
        
        List<Integer> muList = new ArrayList<>(List.of(1, 2));
        muList = new ArrayList<>(List.of(1, 2, 3));
        
        print(muList);
    }
    
    public static void main(String[] args) {
        Example134 example = new Example134();
        example.exec();
    }
}
