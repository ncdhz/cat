import com.github.ncdhz.cat.CatBootstrap;
import com.github.ncdhz.cat.annotation.Component;
import com.github.ncdhz.cat.annotation.Resource;

@Component
public class PrimaryTest {

    @Resource
    private static PrimaryInterface primary;

    public static void main(String[] args){
        CatBootstrap.run(PrimaryTest.class);
        primary.testPrimary();
    }
}
