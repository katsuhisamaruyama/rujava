
import org.jtool.rujava.annotation.Mutable;
import org.jtool.rujava.annotation.Immutable;

public class ExampleWarning102 {
    
    ExampleWarning102() {
    }
    
    void exec() {
        @Immutable int x = 0;
    }
}
