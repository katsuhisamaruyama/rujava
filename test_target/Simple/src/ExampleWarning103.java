
import java.util.List;

import org.jtool.rujava.annotation.Mutable;
import org.jtool.rujava.annotation.Immutable;

public class ExampleWarning103 {
    
    ExampleWarning103() {
    }
    
    @Mutable List exec1() {
        return null;
    }
    
    @Mutable List exec2() {
        return null;
    }
    
    @Mutable List exec3() {
        return null;
    }
    
    @Immutable List exec4() {
        return null;
    }
    
    @Immutable List exec5() {
        return null;
    }
    
    @Immutable List exec6() {
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

class ExampleWarning103Child extends ExampleWarning103 {
    
    ExampleWarning103Child() {
    }
    
    @Override
    @Mutable List exec1() {
        return null;
    }
    
    @Override
    @Immutable List exec2() {
        return null;
    }
    
    @Override
    List exec3() {
        return null;
    }
    
    @Override
    @Mutable List exec4() {
        return null;
    }
    
    @Override
    @Immutable List exec5() {
        return null;
    }
    
    @Override
    List exec6() {
        return null;
    }
    
    @Override
    @Mutable List exec7() {
        return null;
    }
    
    @Override
    @Immutable List exec8() {
        return null;
    }
    
    @Override
    List exec9() {
        return null;
    }
}
