import com.github.ncdhz.cat.annotation.Component;

@Component
public class ResourceImp implements ResourceInterface{

    @Override
    public void testResource() {
        System.out.println("I am 'Resource'");
    }
}
