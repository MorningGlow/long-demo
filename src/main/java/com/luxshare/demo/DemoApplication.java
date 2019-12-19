package com.luxshare.demo;

import com.luxshare.demo.boot.SecondApplicationContextInitializer;
import com.sun.corba.se.spi.servicecontext.SendingContextServiceContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

/**
 * demo start up
 *
 * @author lion hua
 * @since 2019-11-02
 */
@SpringBootApplication
//@ServletComponentScan(basePackages = {"com.luxshare.demo.mvc.filter"})
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);


//        SpringApplication springApplication = new SpringApplication(DemoApplication.class);
//        springApplication.addInitializers(new SecondApplicationContextInitializer());
//        springApplication.run(args);
    }
}
