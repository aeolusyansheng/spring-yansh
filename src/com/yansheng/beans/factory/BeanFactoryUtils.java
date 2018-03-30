package com.yansheng.beans.factory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.yansheng.beans.BeansException;

public abstract class BeanFactoryUtils {

    public static final String GENERATED_BEAN_NAME_SEPARATOR = "#";

    public static boolean isFactoryDereference(String name) {
        return (name != null && name.startsWith(BeanFactory.FACTORY_BEAN_PREFIX));
    }

    public static String transformedBeanName(String name) {
        Assert.notNull(name, "'name' must not be null");
        String beanName = name;
        while (beanName.startsWith(BeanFactory.FACTORY_BEAN_PREFIX)) {
            beanName = beanName.substring(BeanFactory.FACTORY_BEAN_PREFIX.length());
        }
        return beanName;
    }

    public static boolean isGeneratedBeanName(String name) {
        return (name != null && name.contains(GENERATED_BEAN_NAME_SEPARATOR));
    }

    public static String originalBeanName(String name) {
        Assert.notNull(name, "'name' must not be null");
        int seperatorIndex = name.indexOf(GENERATED_BEAN_NAME_SEPARATOR);
        return (seperatorIndex != -1 ? name.substring(0, seperatorIndex) : name);
    }

    public static int countBeansIncludingAncestors(ListableBeanFactory lbf) {
        return beanNamesIncludingAncestors(lbf).length;
    }

    public static String[] beanNamesIncludingAncestors(ListableBeanFactory lbf) {
        return beanNamesForTypeIncludingAncestors(lbf, Object.class);
    }

    @SuppressWarnings("rawtypes")
    public static String[] beanNamesForTypeIncludingAncestors(ListableBeanFactory lbf, Class type) {
        Assert.notNull(lbf, "ListableBeanFactory must not be null");
        String[] result = lbf.getBeanNamesForType(type);
        if (lbf instanceof HierarchicalBeanFactory) {
            HierarchicalBeanFactory hbf = (HierarchicalBeanFactory) lbf;
            if (hbf.getParentBeanFactory() instanceof ListableBeanFactory) {
                String[] parentResult = beanNamesForTypeIncludingAncestors(
                        (ListableBeanFactory) hbf.getParentBeanFactory(), type);
                List<String> resultList = new ArrayList<String>();
                resultList.addAll(Arrays.asList(result));
                for (String beanName : parentResult) {
                    if (!resultList.contains(beanName) && !hbf.containsLocalBean(beanName)) {
                        resultList.add(beanName);
                    }
                }
                result = StringUtils.toStringArray(resultList);
            }
        }
        return result;
    }

    public static <T> Map<String, T> beansOfTypeIncludingAncestors(ListableBeanFactory lbf, Class<T> type)
            throws BeansException {
        Assert.notNull(lbf, "ListableBeanFactory must not be null");
        Map<String, T> result = new LinkedHashMap<String, T>(4);
        result.putAll(lbf.getBeansOfType(type));
        if (lbf instanceof HierarchicalBeanFactory) {
            HierarchicalBeanFactory hbf = (HierarchicalBeanFactory) lbf;
            if (hbf.getParentBeanFactory() instanceof ListableBeanFactory) {
                Map<String, T> parentResult = beansOfTypeIncludingAncestors(
                        (ListableBeanFactory) hbf.getParentBeanFactory(), type);
                for (Map.Entry<String, T> entry : parentResult.entrySet()) {
                    String beanName = entry.getKey();
                    if (!result.containsKey(beanName) && !hbf.containsLocalBean(beanName)) {
                        result.put(beanName, entry.getValue());
                    }
                }
            }
        }
        return result;
    }

    public static <T> Map<String, T> beansOfTypeIncludingAncestors(ListableBeanFactory lbf, Class<T> type,
            boolean includeNonSingletons, boolean allowEagerInit)
            throws BeansException {
        Assert.notNull(lbf, "ListableBeanFactory must not be null");
        Map<String, T> result = new LinkedHashMap<String, T>(4);
        result.putAll(lbf.getBeansOfType(type, includeNonSingletons, allowEagerInit));
        if (lbf instanceof HierarchicalBeanFactory) {
            HierarchicalBeanFactory hbf = (HierarchicalBeanFactory) lbf;
            if (hbf.getParentBeanFactory() instanceof ListableBeanFactory) {
                Map<String, T> parentResult = beansOfTypeIncludingAncestors(
                        (ListableBeanFactory) hbf.getParentBeanFactory(), type, includeNonSingletons, allowEagerInit);
                for (Map.Entry<String, T> entry : parentResult.entrySet()) {
                    String beanName = entry.getKey();
                    if (!result.containsKey(beanName) && !hbf.containsLocalBean(beanName)) {
                        result.put(beanName, entry.getValue());
                    }
                }
            }
        }
        return result;
    }

    public static <T> T beanOfTypeIncludingAncestors(ListableBeanFactory lbf, Class<T> type,
            boolean includeNonSingletons, boolean allowEagerInit)
            throws BeansException {
        Map<String, T> beansOfType = beansOfTypeIncludingAncestors(lbf, type, includeNonSingletons, allowEagerInit);
        return uniqueBean(type, beansOfType);
    }

    public static <T> T beanOfTypeIncludingAncestors(ListableBeanFactory lbf, Class<T> type)
            throws BeansException {
        Map<String, T> beansOfType = beansOfTypeIncludingAncestors(lbf, type);
        return uniqueBean(type, beansOfType);
    }

    public static <T> T beanOfType(ListableBeanFactory lbf, Class<T> type) throws BeansException {
        Assert.notNull(lbf, "ListableBeanFactory must not be null");
        Map<String, T> beansOfType = lbf.getBeansOfType(type);
        return uniqueBean(type, beansOfType);
    }

    public static <T> T beanOfType(ListableBeanFactory lbf, Class<T> type, boolean includeNonSingletons,
            boolean allowEagerInit) throws BeansException {
        Assert.notNull(lbf, "ListableBeanFactory must not be null");
        Map<String, T> beansOfType = lbf.getBeansOfType(type, includeNonSingletons, allowEagerInit);
        return uniqueBean(type, beansOfType);
    }

    private static <T> T uniqueBean(Class<T> type, Map<String, T> matchingBeans) {
        int found = matchingBeans.size();
        if (found == 1) {
            return matchingBeans.values().iterator().next();
        } else if (found > 1) {
            throw new NoUniqueBeanDefinitionException(type, matchingBeans.keySet());
        } else {
            throw new NoSuchBeanDefinitionException(type);
        }
    }

}
