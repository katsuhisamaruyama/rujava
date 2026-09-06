
import org.jtool.rujava.annotation.Owned;
import org.jtool.rujava.annotation.Borrow;

public class ExampleWarning202 {
    
    ExampleWarning202() {
    }
    
    void exec() {
        @Borrow int x = 0;
    }
}
