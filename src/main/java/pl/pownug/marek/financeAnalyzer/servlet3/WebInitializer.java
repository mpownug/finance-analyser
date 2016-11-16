package pl.pownug.marek.financeAnalyzer.servlet3;

import javax.servlet.Filter;
import javax.servlet.ServletContext;

import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import pl.pownug.marek.financeAnalyzer.config.ApplicationConfig;
import pl.pownug.marek.financeAnalyzer.config.HibernateConfig;
import pl.pownug.marek.financeAnalyzer.config.SecurityConfig;
import pl.pownug.marek.financeAnalyzer.config.WebMvcConfig;

public class WebInitializer extends
		AbstractAnnotationConfigDispatcherServletInitializer 
{
	
	@Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

    @Override
    protected Class<?>[] getRootConfigClasses() {
    	return new Class<?>[] {ApplicationConfig.class, HibernateConfig.class, WebMvcConfig.class, SecurityConfig.class};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[] {};
    }

    @Override
    protected Filter[] getServletFilters() {
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        characterEncodingFilter.setForceEncoding(true);

        DelegatingFilterProxy securityFilterChain = new DelegatingFilterProxy("springSecurityFilterChain");

        return new Filter[] {characterEncodingFilter, securityFilterChain};
    }
    
    @Override
    protected void registerDispatcherServlet(ServletContext servletContext) {
        super.registerDispatcherServlet(servletContext);

        servletContext.addListener(new HttpSessionEventPublisher());

    }

}