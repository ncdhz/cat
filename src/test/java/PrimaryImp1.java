import com.github.ncdhz.cat.annotation.Component;
import com.github.ncdhz.cat.annotation.Primary;

@Component
@Primary
public class PrimaryImp1 implements PrimaryInterface{
    @Override
    public void testPrimary() {
        System.out.println("I am 'Primary1");
    }
}
