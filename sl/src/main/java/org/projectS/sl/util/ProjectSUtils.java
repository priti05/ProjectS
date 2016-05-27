package org.projectS.sl.util;

import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.projectS.dbo.reporting.dao.response.RetrieveProjectSTrackingIdResponseTO;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;

public final class ProjectSUtils {
	
	private Logger logger = LogManager.getLogger(ProjectSUtils.class);
	
	public <T> T extractObject(Future<T> objectCall) throws InterruptedException, ExecutionException{
		logger.info("Inside extractObject");
		T t = null;
			if(objectCall!=null){
				while(!objectCall.isDone()){
					Thread.sleep(10);
				}
				t = (T) objectCall.get();
			}	
			logger.info("End of extractObject");
			return t;
	}
	
	public String extracttrackingId(RetrieveProjectSTrackingIdResponseTO retrieveProjectSTrackingIdResponseTO){
		logger.info("Inside extracttrackingId");
		if(retrieveProjectSTrackingIdResponseTO!=null){
			return retrieveProjectSTrackingIdResponseTO.getTrackingId();
		}
		logger.info("End of extracttrackingId");
		return null;
	}
	
	@Async
	public Future<String> hashPassword(String password){
		String hasedPassword = null;
		return new AsyncResult<String>(hasedPassword);
		
	}
	
	public String generateAGRN(){
		Long agrnLong = (long) (10000000 + (new Random().nextFloat()) * 90000000);
		String agrn = String.valueOf(agrnLong);
		return agrn;
	}

}
