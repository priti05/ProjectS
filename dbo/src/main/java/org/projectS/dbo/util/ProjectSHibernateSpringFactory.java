package org.projectS.dbo.util;

import org.hibernate.SessionFactory;
import org.springframework.context.support.AbstractApplicationContext;

public abstract interface ProjectSHibernateSpringFactory
{
  public abstract SessionFactory getSessionFactoryInstance();
  
  public abstract AbstractApplicationContext getApplicationContextInstance();
}
