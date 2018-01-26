package springscope.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springscope.components.ThreadPropagatedScopeWrapper;

@Configuration
public class ScopeConfig {
    /**
     * Этот бин нужно добавить
     */
    @Bean
    public static ThreadPropagatedScopeWrapper scopeWrapper() {
        final ThreadPropagatedScopeWrapper result = new ThreadPropagatedScopeWrapper();
        result.setScopeName(ScopeNames.MAIN);
        return result;
    }
}
