package com.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class App {


    private static final Logger logger = LoggerFactory.getLogger(App.class);


    @Value("${restarts:3}")
    private static int numberOfRestarts;


    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(App.class, args);

        Executor executor = context.getBean(Executor.class);
        runExecutorContinuously(executor);
    }

    private static void runExecutorContinuously(Executor executor) {
        int fails = 0;

        while (fails < numberOfRestarts + 1) {
            try {
                runExecutor(executor);
            } catch (Exception e) {
                logger.error("Executor encountered error", e);
                fails++;

                if (fails < numberOfRestarts + 1) {
                    logger.debug("Restarting executor...");
                }
            }
        }
    }

    private static void runExecutor(Executor executor) {
        ((BadooExecutor) executor)
                .checkPrerequisites()
                .prepareExecution()
                .executeSite();

        executor.closeSite();
    }

}
