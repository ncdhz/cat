import com.github.ncdhz.cat.CatBootstrap;
import com.github.ncdhz.cat.annotation.Component;

@Component
public class ComponentTest {
    public ComponentTest(){
        System.out.println("I loaded it into the cat container");
    }
    public static void main(String[] args){
        CatBootstrap.run(ComponentTest.class);
    }
}
