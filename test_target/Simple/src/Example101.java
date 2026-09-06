
import java.util.List;
import java.util.ArrayList;

import org.jtool.rujava.annotation.Mutable;
import org.jtool.rujava.annotation.Immutable;

public class Example101 {
    
    Example101() {
    }
    
    void exec() {
        List<Integer> list = new ArrayList<>(List.of(1, 2));
        
        System.out.println(list);
        
        list.add(3);
        
        System.out.println(list);
    }
    
    public static void main(String[] args) {
        Example101 example = new Example101();
        example.exec();
    }
}
