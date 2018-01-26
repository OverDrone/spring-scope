package springscope.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
@EnableScheduling
public class AsyncConfig {

    /**
     * Этот бин в БРОГИ уже определен. Можно переделать как в данном примере
     */
    @Bean
    public static ThreadPoolTaskExecutor mainExecutor() {
        final ThreadPoolTaskExecutor result = new ThreadPoolTaskExecutor();
        result.setMaxPoolSize(5);
        return result;
    }
    
    /**
     * Этот бин в БРОГИ определен по-умолчанию и там poolSize=1, поэтому можно перейти на данный бин
     */
    @Bean
    public static ThreadPoolTaskScheduler mainScheduler() {
        final ThreadPoolTaskScheduler result = new ThreadPoolTaskScheduler();
        result.setPoolSize(5);
        return result;
    }
}
