package com.ps.beans.ctr;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by iuliana.cosmina on 3/26/16.
 */
public class CIBeansTest {
    private Logger logger = LoggerFactory.getLogger(CIBeansTest.class);

    @Test
    public void testConfig() {
        // ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:spring/ctr/sample-config-01.xml");
        ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:spring/ctr/sample-config-02.xml");

        logger.info(" --- All beans in context --- ");
        for(String beanName :  ctx.getBeanDefinitionNames()) {
            logger.info("Bean " + beanName + " of type " + ctx.getBean(beanName).getClass().getSimpleName());

            if (beanName.startsWith("complexBean")) {
                logger.info("found bean: " + beanName);

                ComplexBeanImpl complexBean;
                ComplexBean2Impl complexBean2;
                switch(beanName) {
                    case "complexBean0":
                        complexBean = ctx.getBean("complexBean0", ComplexBeanImpl.class);
                        assertNotNull(complexBean.getSimpleBean().getClass().getSimpleName());
                        break;
                    case "complexBean1":
                        complexBean = ctx.getBean("complexBean1", ComplexBeanImpl.class);
                        assertNotNull(complexBean.getSimpleBean().getClass().getSimpleName());
                        assertTrue(complexBean.isComplex());
                        break;
                    case "complexBean2":
                        complexBean2 = ctx.getBean("complexBean2", ComplexBean2Impl.class);
                        assertNotNull(complexBean2.getSimpleBean1());
                        assertNotNull(complexBean2.getSimpleBean2());
                        break;
                }
            }
        }
    }
}
