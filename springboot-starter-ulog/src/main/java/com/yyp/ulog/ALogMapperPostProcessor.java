package com.yyp.ulog;

import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyResourceConfigurer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class ALogMapperPostProcessor extends PropertyResourceConfigurer {

    private final String LogMapperPath = "com.yyp.ulog.dao";

    @Override
    protected void processProperties(ConfigurableListableBeanFactory beanFactory, Properties props) throws BeansException {
        String[] beanDefinitionNames = beanFactory.getBeanDefinitionNames();
        if (beanDefinitionNames.length > 0) {
            BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanDefinitionNames[0]);
            MutablePropertyValues propertyValues = beanDefinition.getPropertyValues();
            PropertyValue basePackage = propertyValues.getPropertyValue("basePackage");
            if (basePackage != null) {
                String[] strings = StringUtils.tokenizeToStringArray((String) basePackage.getValue(), ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS);
                List<String> basePackages = Arrays.stream(strings).collect(Collectors.toList());
                basePackages.add(LogMapperPath);
                propertyValues.addPropertyValue("basePackage", StringUtils.collectionToCommaDelimitedString(basePackages));
            }
        }
    }

}
