import com.github.ncdhz.cat.CatBootstrap;
import com.github.ncdhz.cat.annotation.Bean;
import com.github.ncdhz.cat.annotation.Component;
import com.github.ncdhz.cat.annotation.Resource;
import com.github.ncdhz.cat.util.Chopsticks;

@Component
public class BeanTest {

    public static void main(String[] args){
        CatBootstrap.run(BeanTest.class);
        Chopsticks chopsticks = Chopsticks.getChopsticks();

        String testBean = chopsticks.getBean(String.class);
        System.out.println(testBean);

        String testBean1 = chopsticks.getBean("test-bean");
        System.out.println(testBean1);
    }
    @Bean(name = "test-bean")
    private String testBean(){
        return "test bean";
    }

    @Bean
    @Resource
    private int testBean1(String test){
        System.out.println(test+"1");
        return 1;
    }
}
