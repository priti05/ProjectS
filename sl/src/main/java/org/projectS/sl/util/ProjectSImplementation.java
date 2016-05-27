package org.projectS.sl.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

enum ProjectSImplementation implements ProjectS {

	INSTANCE;
	private final AbstractApplicationContext applicationContext = buildAppContext();
	

	public final AbstractApplicationContext getApplicationContextInstance(){
		return applicationContext;
	}
	
	
	private final AbstractApplicationContext buildAppContext(){
		Logger logger = LogManager.getLogger(ProjectSImplementation.class.getName());
		try{
			logger.info("Initializing ApplicationContext");
			AbstractApplicationContext applicationContext = new ClassPathXmlApplicationContext("../config/application-context.xml");
			applicationContext.registerShutdownHook();
			return applicationContext;
		}catch(Throwable te){
			logger.fatal("ApplicationContext hasn't been initialized" + te.getMessage());
			logger.fatal("stackTrace::",te);
			logger.fatal("end of buildAppContext");
			throw new ExceptionInInitializerError(te);
		}
	}
	
}
