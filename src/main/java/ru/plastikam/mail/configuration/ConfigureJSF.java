package ru.plastikam.mail.configuration;

import com.sun.faces.config.FacesInitializer;
import org.primefaces.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.CustomScopeConfigurer;
import org.springframework.boot.context.embedded.ServletContextInitializer;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
import org.springframework.web.context.ContextLoaderListener;
import ru.plastikam.mail.sys.ViewScope;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@Configuration
//@Order(1)
public class ConfigureJSF implements ServletContextInitializer {
    private static final Logger logger = LoggerFactory.getLogger(ConfigureJSF.class);

    @Bean
    public OpenEntityManagerInViewFilter openEntityManagerInViewFilter() {
        return new OpenEntityManagerInViewFilter();
    }

//    @Bean
//    public ContextLoaderListener createContextLoaderListener() {
//        return new ContextLoaderListener();
//    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {

        logger.info("Конфигурация JSF ...");

        servletContext.setInitParameter("com.sun.faces.forceLoadConfiguration", Boolean.TRUE.toString());
        servletContext.setInitParameter("com.sun.faces.expressionFactory", "com.sun.el.ExpressionFactoryImpl");

        servletContext.setInitParameter("javax.faces.DEFAULT_SUFFIX", ".xhtml");
        servletContext.setInitParameter("javax.faces.PARTIAL_STATE_SAVING_METHOD", "true");
        servletContext.setInitParameter("javax.faces.DATETIMECONVERTER_DEFAULT_TIMEZONE_IS_SYSTEM_TIMEZONE", "true");
        servletContext.setInitParameter("javax.faces.PROJECT_STAGE", "Development");
        servletContext.setInitParameter("facelets.DEVELOPMENT", "true");
        servletContext.setInitParameter("javax.faces.FACELETS_REFRESH_PERIOD", "1");

        servletContext.setInitParameter(Constants.ContextParams.THEME, "afterwork");
        servletContext.setInitParameter(Constants.ContextParams.FONT_AWESOME, "true");
    }

    @SuppressWarnings("serial")
    @Bean
    public CustomScopeConfigurer customScopeConfigurer() {
        CustomScopeConfigurer csc = new CustomScopeConfigurer();
        csc.setScopes(new HashMap<String, Object>() {
            {
                put("view", new ViewScope());
            }
        });
        return csc;
    }

    @Bean
    public ServletRegistrationBean facesServletRegistration() {

        ServletRegistrationBean servletRegistrationBean = new JsfServletRegistrationBean();

        return servletRegistrationBean;
    }

    public class JsfServletRegistrationBean extends ServletRegistrationBean {

        public JsfServletRegistrationBean() {
            super();
        }

        @Override
        public void onStartup(ServletContext servletContext) throws ServletException {

            FacesInitializer facesInitializer = new FacesInitializer();

            Set<Class<?>> clazz = new HashSet<Class<?>>();
            clazz.add(ConfigureJSF.class);
            facesInitializer.onStartup(clazz, servletContext);
        }
    }
}