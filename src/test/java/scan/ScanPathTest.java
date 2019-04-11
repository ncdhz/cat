package scan;

import com.github.ncdhz.cat.CatBootstrap;
import com.github.ncdhz.cat.annotation.ScanPath;

@ScanPath(paths = "/")
public class ScanPathTest {
    public static void main(String[] args){
        CatBootstrap.run(ScanPathTest.class);
    }
}
