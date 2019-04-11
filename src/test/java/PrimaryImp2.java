import com.github.ncdhz.cat.annotation.Component;

@Component
public class PrimaryImp2 implements PrimaryInterface{
    @Override
    public void testPrimary() {
        System.out.println("I am 'Primary2");
    }
}
