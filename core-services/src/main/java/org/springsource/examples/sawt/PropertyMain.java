package org.springsource.examples.sawt;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanExpressionContext;
import org.springframework.beans.factory.config.BeanExpressionResolver;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.expression.StandardBeanExpressionResolver;
import org.springframework.expression.spel.standard.SpelExpressionParser;


public class PropertyMain {
	
 
	public static void main(String[] argsImAPirate) throws Throwable {
		class NoopBeanExpressionResolver implements BeanExpressionResolver {

			@Override
			public Object evaluate(String value,
					BeanExpressionContext evalContext) throws BeansException {
 				return null;
			}
			
		}
		
		
	  AnnotationConfigApplicationContext applicationContext
	    = new AnnotationConfigApplicationContext() {
			protected void prepareBeanFactory(ConfigurableListableBeanFactory beanFactory) {
 				super.prepareBeanFactory (beanFactory) ;
				beanFactory.setBeanExpressionResolver(new NoopBeanExpressionResolver());
			}
	  };
	  
		   
		
	}
}
