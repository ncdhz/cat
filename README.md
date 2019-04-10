# cat

这是一个类容器
内置了Bean，Component，Primary，Resource，RunMethod，ScanPath注解

### @ScanPath
此注解只能用于启动此框架的类上，用于指定额外需要扫描的包。如果没有指定默认只会扫描加载类的包下的所有类（包含包下包里面的类）。

### @Component
用在类上面含有此注解的类会被加载到cat的容器类里面通过 Chopsticks.getBean 可以获取

### @Retention
此注解用于注入类，含有此注解类属性，方法都会在cat容器中找到合适的类注入。

### @Primary
两个同父类的子类需要被注入时（也就是有@Retention修饰其父类属性）含有此注解的优先级最高如果都没有此注解默认加载cat容器中找到的第一个子类。

### @Bean
用在方法上 并把方法的返回值保存在cat的容器类里面通过 Chopsticks.getBean 可以获取。用@Bean修饰的类可以含有一个参数，
含有参数时该方法需要被@Retention注解。

### @RunMethod
此注解用于运行一个方法，也就是单纯的执行含有此注解的方法，返回值默认会丢弃。





