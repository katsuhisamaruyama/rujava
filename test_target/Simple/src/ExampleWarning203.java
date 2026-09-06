
import java.util.List;

import org.jtool.rujava.annotation.Owned;
import org.jtool.rujava.annotation.Borrow;

public class ExampleWarning203 {
    
    ExampleWarning203() {
    }
    
    @Owned List exec1() {
        return null;
    }
    
    @Owned List exec2() {
        return null;
    }
    
    @Owned List exec3() {
        return null;
    }
    
    @Borrow List exec4() {
        return null;
    }
    
    @Borrow List exec5() {
        return null;
    }
    
    @Borrow List exec6() {
        return null;
    }
    
    List exec7() {
        return null;
    }
    
    List exec8() {
        return null;
    }
    
    List exec9() {
        return null;
    }
}

class ExampleWarning203Child extends ExampleWarning203 {
    
    ExampleWarning203Child() {
    }
    
    @Override
    @Owned List exec1() {
        return null;
    }
    
    @Override
    @Borrow List exec2() {
        return null;
    }
    
    @Override
    List exec3() {
        return null;
    }
    
    @Override
    @Owned List exec4() {
        return null;
    }
    
    @Override
    @Borrow List exec5() {
        return null;
    }
    
    @Override
    List exec6() {
        return null;
    }
    
    @Override
    @Owned List exec7() {
        return null;
    }
    
    @Override
    @Borrow List exec8() {
        return null;
    }
    
    @Override
    List exec9() {
        return null;
    }
}
