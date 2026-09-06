
import java.util.List;

import org.jtool.rujava.annotation.Owned;
import org.jtool.rujava.annotation.Borrow;

public class ExampleWarning205 {
    
    ExampleWarning205() {
    }
    
    void exec1(@Owned List list) {
    }
    
    void exec2(@Owned List list) {
    }
    
    void exec3(@Owned List list) {
    }
    
    void exec4(@Borrow List list) {
    }
    
    void exec5(@Borrow List list) {
    }
    
    void exec6(@Borrow List list) {
    }
    
    void exec7(List list) {
    }
    
    void exec8(List list) {
    }
    
    void exec9(List list) {
    }
    
    void exec10(@Owned List list1, @Owned List list2) {
    }
    
    void exec11(@Owned List list1, @Owned List list2) {
    }
    
    void exec12(@Owned List list1, @Borrow List list2) {
    }
    
    void exec13(@Owned List list1, @Borrow List list2) {
    }
    
    void exec14(@Owned List list1, @Borrow List list2) {
    }
    
    void exec15(@Owned List list1, @Borrow List list2) {
    }
    
    void exec16(@Owned List list1, @Borrow List list2) {
    }
}

class ExampleWarning205Child extends ExampleWarning205 {
    
    ExampleWarning205Child() {
    }
    
    @Override
    void exec1(@Owned List list) {
    }
    
    @Override
    void exec2(@Borrow List list) {
    }
    
    @Override
    void exec3(List list) {
    }
    
    @Override
    void exec4(@Owned List list) {
    }
    
    @Override
    void exec5(@Borrow List list) {
    }
    
    @Override
    void exec6(List list) {
    }
    
    @Override
    void exec7(@Owned List list) {
    }
    
    @Override
    void exec8(@Borrow List list) {
    }
    
    @Override
    void exec9(List list) {
    }
    
    @Override
    void exec10(@Owned List list1, @Owned List list2) {
    }
    
    @Override
    void exec11(@Owned List list1, @Borrow List list2) {
    }
    
    @Override
    void exec12(@Owned List list1, @Borrow List list2) {
    }
    
    @Override
    void exec13(@Borrow List list1, @Borrow List list2) {
    }
    
    @Override
    void exec14(@Owned List list1, @Owned List list2) {
    }
    
    @Override
    void exec15(@Borrow List list1, @Owned List list2) {
    }
    
    @Override
    void exec16(@Borrow List list1, List list2) {
    }
}
