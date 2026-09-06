
import java.util.List;

import org.jtool.rujava.annotation.Owned;
import org.jtool.rujava.annotation.Borrow;

public class ExampleWarning206 {
    
    ExampleWarning206() {
    }
    
    void exec(@Owned @Borrow List list1, @Owned List list2) {
    }
}
