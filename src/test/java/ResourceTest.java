import com.github.ncdhz.cat.CatBootstrap;
import com.github.ncdhz.cat.annotation.Component;
import com.github.ncdhz.cat.annotation.Resource;

@Component
public class ResourceTest {
    @Resource
    private static ResourceInterface resource;

    public static void main(String[] args){
        CatBootstrap.run(ResourceTest.class);
        resource.testResource();
    }
}
