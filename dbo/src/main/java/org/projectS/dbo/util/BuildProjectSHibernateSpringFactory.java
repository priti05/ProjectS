package org.projectS.dbo.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

 enum BuildProjectSHibernateSpringFactory
  implements ProjectSHibernateSpringFactory
{
  INSTANCE;
  
  private final SessionFactory sessionFactory = buildHibernateSessionFactory();
  private final AbstractApplicationContext applicationContext = buildAppContext();
  
  public final SessionFactory getSessionFactoryInstance()
  {
    return this.sessionFactory;
  }
  
  private final SessionFactory buildHibernateSessionFactory()
  {
    Logger logger = LogManager.getLogger(BuildProjectSHibernateSpringFactory.class.getName());
    logger.info("inside buildHibernateSessionFactory");
    try
    {
      Configuration configuration = new Configuration();
      configuration.configure("/config/projectS-dbo.cfg.xml");
      ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(
        configuration.getProperties()).build();
      return configuration.buildSessionFactory(serviceRegistry);
    }
    catch (Throwable te)
    {
      logger.fatal("Session Factory hasn't been initialized" + te.getMessage());
      logger.fatal("stackTrace::", te);
      logger.fatal("end of buildHibernateSessionFactory");
      throw new ExceptionInInitializerError(te);
    }
  }
  
  public final AbstractApplicationContext getApplicationContextInstance()
  {
    return this.applicationContext;
  }
  
  private final AbstractApplicationContext buildAppContext()
  {
    Logger logger = LogManager.getLogger(BuildProjectSHibernateSpringFactory.class.getName());
    try
    {
      logger.info("Initializing ApplicationContext");
      AbstractApplicationContext applicationContext = new ClassPathXmlApplicationContext("/config/application-context.xml");
      applicationContext.registerShutdownHook();
      return applicationContext;
    }
    catch (Throwable te)
    {
      logger.fatal("ApplicationContext hasn't been initialized" + te.getMessage());
      logger.fatal("stackTrace::", te);
      logger.fatal("end of buildAppContext");
      throw new ExceptionInInitializerError(te);
    }
  }
}
