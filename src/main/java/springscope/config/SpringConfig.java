package springscope.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

import springscope.BeansPackagePlaceholder;

/**
 * Уже есть
 */
@Configuration
@ComponentScan(basePackageClasses = BeansPackagePlaceholder.class)
@ImportResource("classpath:/spring/main.xml")
public class SpringConfig {
}
