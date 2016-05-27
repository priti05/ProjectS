package org.projectS.sl.registration;

import java.util.concurrent.Future;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.projectS.dbo.error.exception.ProjectSException;
import org.projectS.dbo.reporting.dao.ReportingDAO;
import org.projectS.dbo.reporting.dao.request.RetrieveProjectSTrackingIdRequestTO;
import org.projectS.dbo.reporting.dao.request.ThrowProjectSExceptionRequestTO;
import org.projectS.dbo.reporting.dao.request.UpdateProjectSTrackingIdRequestTO;
import org.projectS.dbo.reporting.dao.response.RetrieveProjectSTrackingIdResponseTO;
import org.projectS.sl.registration.helper.RegistrationConstant;
import org.projectS.sl.registration.helper.RegistrationHelper;
import org.projectS.sl.registration.request.RegisterProfileRequestTO;
import org.projectS.sl.registration.response.RegisterProfileResponseTO;
import org.projectS.sl.util.ProjectSUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync
final class Registration implements IRegistration {

	private Logger logger = LogManager.getLogger(Registration.class);
	
	@Autowired
	private ReportingDAO reportingDAO;
	
	@Autowired
	private RegistrationHelper registrationHelper;
	
	@Autowired
	private ProjectSUtils projectSUtils;
	
	public RegisterProfileResponseTO register(RegisterProfileRequestTO registerProfileRequestTO) throws ProjectSException {
		logger.info("Inside register");
		RegisterProfileResponseTO registerProfileResponseTO=null;
		Future<RetrieveProjectSTrackingIdResponseTO>  retrieveTrackingIdAsyncCall = null;
		String trckId = null;
		try{
			RetrieveProjectSTrackingIdRequestTO retrieveProjectSTrackingIdRequestTO = new RetrieveProjectSTrackingIdRequestTO(
					registerProfileRequestTO,RegistrationConstant.API);
			retrieveTrackingIdAsyncCall = reportingDAO.retrieveTrackingId(
					retrieveProjectSTrackingIdRequestTO);
			
			  boolean registered = registrationHelper.registerHelper(registerProfileRequestTO, retrieveTrackingIdAsyncCall);
			trckId  = projectSUtils.extracttrackingId((RetrieveProjectSTrackingIdResponseTO)
					projectSUtils.extractObject(retrieveTrackingIdAsyncCall));
			registerProfileResponseTO = new RegisterProfileResponseTO(trckId);
			registerProfileResponseTO.setRegistered(registered);
			UpdateProjectSTrackingIdRequestTO updateProjectSTrackingIdRequestTO = new UpdateProjectSTrackingIdRequestTO(trckId,registerProfileResponseTO);
			reportingDAO.updateTrackingIdTable(updateProjectSTrackingIdRequestTO);
		}catch(Exception e){
			logger.error("Exception at register::", e.getMessage());
			logger.error(e);
			try {
				trckId  = projectSUtils.extracttrackingId((RetrieveProjectSTrackingIdResponseTO)
						projectSUtils.extractObject(retrieveTrackingIdAsyncCall));
			} catch (Exception e1) {
				logger.error(e1.getMessage());
				logger.error(e1);
			} 
			ThrowProjectSExceptionRequestTO throwProjectSExceptionRequestTO = new 
					ThrowProjectSExceptionRequestTO(RegistrationConstant.PROJS_REG_000, trckId, RegistrationConstant.API);
			throw reportingDAO.throwProjectSException(throwProjectSExceptionRequestTO);
		}
		logger.info("End of register");
		return registerProfileResponseTO;
	}

}
