package ru.doublebyte.amznsm;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import ru.doublebyte.amznsm.services.ItemStorage;

@Configuration
public class MainConfiguration {

    @Bean
    public TaskScheduler taskScheduler() {
        return new ThreadPoolTaskScheduler();
    }

    @Bean
    public ItemStorage itemStorage() {
        return new ItemStorage();
    }

}
