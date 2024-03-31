package by.khodyko.different.securities.basic;


import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.boot.actuate.autoconfigure.endpoint.EndpointAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.CorsEndpointProperties;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;
import org.springframework.boot.actuate.autoconfigure.health.HealthContributorAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.health.HealthEndpointAutoConfiguration;

import org.springframework.boot.actuate.autoconfigure.info.InfoEndpointAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.logging.LoggersEndpointAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.management.HeapDumpWebEndpointAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.management.ThreadDumpEndpointAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.metrics.export.prometheus.PrometheusMetricsExportAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.web.server.ManagementContextAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.web.servlet.ServletManagementContextAutoConfiguration;
import org.springframework.boot.actuate.endpoint.ExposableEndpoint;
import org.springframework.boot.actuate.endpoint.web.*;
import org.springframework.boot.actuate.endpoint.web.annotation.ControllerEndpointsSupplier;
import org.springframework.boot.actuate.endpoint.web.annotation.ServletEndpointsSupplier;
import org.springframework.boot.actuate.endpoint.web.servlet.WebMvcEndpointHandlerMapping;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletPath;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring6.ISpringTemplateEngine;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@EnableWebMvc
@Import({SecurityConfig.class})
@ImportAutoConfiguration({
        EndpointAutoConfiguration.class,
        WebEndpointAutoConfiguration.class,
        ServletManagementContextAutoConfiguration.class,
        ManagementContextAutoConfiguration.class,

        HealthContributorAutoConfiguration.class,

        InfoEndpointAutoConfiguration.class,
        HealthEndpointAutoConfiguration.class,

        HeapDumpWebEndpointAutoConfiguration.class,
        ThreadDumpEndpointAutoConfiguration.class,
        LoggersEndpointAutoConfiguration.class,
        PrometheusMetricsExportAutoConfiguration.class,
})
@EnableConfigurationProperties(CorsEndpointProperties.class)
@ComponentScan(basePackages = {"by.khodyko"})
public class AppConfig implements WebMvcConfigurer {

    private final ApplicationContext applicationContext;

    @Autowired
    public AppConfig(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Bean
    public ITemplateResolver templateResolver() {
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setApplicationContext(applicationContext);
        templateResolver.setPrefix("/WEB-INF/views/");
        templateResolver.setSuffix(".html");
        return templateResolver;
    }

    @Bean
    public ISpringTemplateEngine templateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver());
        templateEngine.setEnableSpringELCompiler(true);
        return templateEngine;
    }

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
        resolver.setTemplateEngine(templateEngine());
        registry.viewResolver(resolver);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
    }

    @Bean //taken from WebMvcEndpointManagementContextConfiguration.class
    public WebMvcEndpointHandlerMapping webEndpointServletHandlerMapping(WebEndpointsSupplier webEndpointsSupplier,
                                                                         ServletEndpointsSupplier servletEndpointsSupplier, ControllerEndpointsSupplier controllerEndpointsSupplier,
                                                                         EndpointMediaTypes endpointMediaTypes, CorsEndpointProperties corsProperties,
                                                                         WebEndpointProperties webEndpointProperties) {
        List<ExposableEndpoint<?>> allEndpoints = new ArrayList<>();
        Collection<ExposableWebEndpoint> webEndpoints = webEndpointsSupplier.getEndpoints();
        allEndpoints.addAll(webEndpoints);
        allEndpoints.addAll(servletEndpointsSupplier.getEndpoints());
        allEndpoints.addAll(controllerEndpointsSupplier.getEndpoints());
        EndpointMapping endpointMapping = new EndpointMapping(webEndpointProperties.getBasePath());
        return new WebMvcEndpointHandlerMapping(endpointMapping,
                webEndpoints,
                endpointMediaTypes,
                corsProperties.toCorsConfiguration(),
                new EndpointLinksResolver(allEndpoints, webEndpointProperties.getBasePath()),
                true);
    }

    @Bean
    DispatcherServletPath dispatcherServletPath() {
        return new DispatcherServletPath() {

            @Override
            public String getPath() {
                return "/basic";
            }

            @Override
            public String getRelativePath(String path) {
                return DispatcherServletPath.super.getRelativePath(path);
            }

            @Override
            public String getPrefix() {
                return DispatcherServletPath.super.getPrefix();
            }

            @Override
            public String getServletUrlMapping() {
                return DispatcherServletPath.super.getServletUrlMapping();
            }
        };
    }

}
