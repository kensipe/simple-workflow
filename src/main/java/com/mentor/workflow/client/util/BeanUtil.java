package com.mentor.workflow.client.util;

import com.mentor.workflow.client.exception.BeanCreationException;
//import org.apache.commons.beanutils.BeanUtils;
//import org.apache.commons.beanutils.ConvertUtils;
//import org.apache.commons.beanutils.PropertyUtils;
//import org.apache.commons.beanutils.converters.DateConverter;
//import org.apache.commons.beanutils.converters.LongConverter;
import org.apache.log4j.Logger;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author: ksipe
 */
public class BeanUtil {

    static Logger logger = Logger.getLogger(BeanUtil.class);
//    static {
//    	 ConvertUtils.register(new LongConverter(null), Long.class);
//		 ConvertUtils.register(new DateConverter(null), Date.class);
//    }
//
//    public static Map<String, Object> describe(Object bean) {
//
//        if (bean == null) {
//            return new HashMap<String, Object>();
//        }
//
//        if (logger.isDebugEnabled()) {
//            logger.debug("Describing bean: " + bean.getClass().getName());
//        }
//
//        Map<String, Object> description = new HashMap<String, Object>();
//
//        PropertyDescriptor[] descriptors = PropertyUtils.getPropertyDescriptors(bean);
//        for (PropertyDescriptor descriptor : descriptors) {
//            String name = descriptor.getName();
//            if (PropertyUtils.getReadMethod(descriptor) != null && !"class".equals(name)) {
//
//                try {
//                    Object value = PropertyUtils.getProperty(bean, name);
//                    description.put(name, value);
//                } catch (Exception e) {
//                    // these would be reflection or illegal access exceptions which shouldn't occur based on
//                    // carefully crafted defensive programming techniques
//                    logger.error(e);
//                }
//            }
//        }
//
//        return description;
//    }
//
//
//    public static void populate(Object bean, Map<String, Object> properties) {
//
//        try {
//
//            // ** common's BeanUtils has a bug working with dates... can't convert null, or strings to date
//            // providing fix for null values
//            if ((bean == null) || (properties == null)) {
//                return;
//            }
//            if (logger.isDebugEnabled()) {
//                logger.debug("populate(" + bean + ", " + properties + ")");
//            }
//
//            for (String key: properties.keySet()) {
//
//                if (properties.get(key) == null) {
//                    if (logger.isDebugEnabled() && key != null) {
//                        logger.debug("Setting property value for: " + key + " to null");
//                    }
//                }
//                // Perform the assignment for this property
//                BeanUtils.setProperty(bean, key, properties.get(key));
//
//            }
//
//        } catch (Exception e) {
//            // these would be reflection or illegal access exceptions which shouldn't occur (read above comments)
//            logger.error(e);
//        }
//    }
//
//
//    public static Map<String, Object> populateWithRemainder(Object bean, Map<String, Object> properties) {
//
//    	if (properties == null)
//			return new HashMap<String, Object>();
//		if (bean == null )
//			return properties;
//
//        if (logger.isDebugEnabled()) {
//            logger.debug("populateWithRemainder (" + bean + ", " + properties + ")");
//        }
//
//        populate(bean, properties);
//
//        return reduceToRemainderProperties(bean, properties);
//
//    }
//
//    private static Map<String, Object> reduceToRemainderProperties(Object bean, Map<String, Object> properties) {
//
//        Map<String, Object> overflow = new HashMap<String, Object>();
//        PropertyDescriptor[] descriptors = PropertyUtils.getPropertyDescriptors(bean);
//        Set<String> beanPropertySet = new HashSet<String>();
//
//        for (PropertyDescriptor descriptor : descriptors) {
//            beanPropertySet.add(descriptor.getName());
//        }
//
//        Set<String> propertySet = properties.keySet();
//        propertySet.removeAll(beanPropertySet);
//
//        for (String propertyName : propertySet) {
//            overflow.put(propertyName, properties.get(propertyName));
//        }
//
//        return overflow;
//    }

    public static Object instantiateBean(Class beanClass) {
        Object entityBean = null;
        try {
            if (beanClass != null)
                entityBean = beanClass.newInstance();
        } catch (Exception e) {
            String errorMessge = "failure to create class: " + beanClass.getName();
            logger.error(errorMessge, e);
            throw new BeanCreationException(errorMessge);
        }
        return entityBean;
    }

    public static void setPrivateProperty(Object bean, String propertyName, Object value) {

        try {
            Field field = bean.getClass().getDeclaredField(propertyName);
            field.setAccessible(true);
            field.set(bean, value);
            field.setAccessible(false);           

        } catch (Exception e) {
                 logger.debug("");
        }
    }
}
