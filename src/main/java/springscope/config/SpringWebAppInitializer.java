package springscope.config;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.AbstractContextLoaderInitializer;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

/**
 * Уже есть
 */
public class SpringWebAppInitializer extends AbstractContextLoaderInitializer implements WebApplicationInitializer {

    @Override
    protected WebApplicationContext createRootApplicationContext() {
        final AnnotationConfigWebApplicationContext result = new AnnotationConfigWebApplicationContext();
        result.register(SpringConfig.class);
        return result;
    }
}
