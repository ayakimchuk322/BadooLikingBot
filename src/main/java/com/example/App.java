package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class App {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(App.class, args);

        BadooExecutor executor = (BadooExecutor) context.getBean(Executor.class);
        executor
                .checkPrerequisites()
                .prepareExecution()
                .executeSite();

        executor.closeSite();
    }

}
