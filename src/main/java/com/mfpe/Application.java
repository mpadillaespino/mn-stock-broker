package com.mfpe;

import io.micronaut.runtime.Micronaut;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;

@OpenAPIDefinition(
    info = @Info(
        title = "mn-stock-broker",
        version = "0.1",
        description = "Micronaut course",
        license = @License(name = "MIT")
    )
)
public class Application {

    public static void main(String[] args) {
        Micronaut.run(Application.class, args);

//        final ApplicationContext context = Micronaut.run(Application.class, args);
//        final HelloService service = context.getBean(HelloService.class);
//        System.out.println(service.greeting());
//        context.close();
    }
}
