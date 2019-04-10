import com.github.ncdhz.cat.CatBootstrap;
import com.github.ncdhz.cat.annotation.Component;
import com.github.ncdhz.cat.annotation.RunMethod;

@Component
public class RunMethodTest {

    public static void main(String[] args){
        CatBootstrap.run(RunMethodTest.class);
    }

    @RunMethod
    protected void testRunMethod(){
        System.out.println("I'm RunMethod");
    }
}
