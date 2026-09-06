
import java.util.List;

import org.jtool.rujava.annotation.Mutable;
import org.jtool.rujava.annotation.Immutable;

public class ExampleWarning105 {
    
    ExampleWarning105() {
    }
    
    void exec1(@Mutable List list) {
    }
    
    void exec2(@Mutable List list) {
    }
    
    void exec3(@Mutable List list) {
    }
    
    void exec4(@Immutable List list) {
    }
    
    void exec5(@Immutable List list) {
    }
    
    void exec6(@Immutable List list) {
    }
    
    void exec7(List list) {
    }
    
    void exec8(List list) {
    }
    
    void exec9(List list) {
    }
    
    void exec10(@Mutable List list1, @Mutable List list2) {
    }
    
    void exec11(@Mutable List list1, @Mutable List list2) {
    }
    
    void exec12(@Mutable List list1, @Immutable List list2) {
    }
    
    void exec13(@Mutable List list1, @Immutable List list2) {
    }
    
    void exec14(@Mutable List list1, @Immutable List list2) {
    }
    
    void exec15(@Mutable List list1, @Immutable List list2) {
    }
    
    void exec16(@Mutable List list1, @Immutable List list2) {
    }
}

class ExampleWarning105Child extends ExampleWarning105 {
    
    ExampleWarning105Child() {
    }
    
    @Override
    void exec1(@Mutable List list) {
    }
    
    @Override
    void exec2(@Immutable List list) {
    }
    
    @Override
    void exec3(List list) {
    }
    
    @Override
    void exec4(@Mutable List list) {
    }
    
    @Override
    void exec5(@Immutable List list) {
    }
    
    @Override
    void exec6(List list) {
    }
    
    @Override
    void exec7(@Mutable List list) {
    }
    
    @Override
    void exec8(@Immutable List list) {
    }
    
    @Override
    void exec9(List list) {
    }
    
    @Override
    void exec10(@Mutable List list1, @Mutable List list2) {
    }
    
    @Override
    void exec11(@Mutable List list1, @Immutable List list2) {
    }
    
    @Override
    void exec12(@Mutable List list1, @Immutable List list2) {
    }
    
    @Override
    void exec13(@Immutable List list1, @Immutable List list2) {
    }
    
    @Override
    void exec14(@Mutable List list1, @Mutable List list2) {
    }
    
    @Override
    void exec15(@Immutable List list1, @Mutable List list2) {
    }
    
    @Override
    void exec16(@Immutable List list1, List list2) {
    }
}
