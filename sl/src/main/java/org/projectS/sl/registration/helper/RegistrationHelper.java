package org.projectS.sl.registration.helper;

import java.util.concurrent.Future;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.projectS.dbo.error.exception.ProjectSException;
import org.projectS.dbo.reporting.dao.ReportingDAO;
import org.projectS.dbo.reporting.dao.request.ThrowProjectSExceptionRequestTO;
import org.projectS.dbo.reporting.dao.response.RetrieveProjectSTrackingIdResponseTO;
import org.projectS.sl.enumHelper.Sex;
import org.projectS.sl.registration.request.RegisterProfileRequestTO;
import org.projectS.sl.util.ProjectSUtils;
import org.projectX.dbo.enumHelper.RegistrationVerificationTypeEnum;
import org.projectX.dbo.enumHelper.SexEnum;
import org.projectX.dbo.login.dao.LoginDAO;
import org.projectX.dbo.login.dao.request.InsertLoginBiographyRequestTO;
import org.projectX.dbo.login.dao.request.InsertLoginInformationRequestTO;
import org.projectX.dbo.login.dao.response.InsertLoginBiographyResponseTO;
import org.projectX.dbo.login.dao.response.InsertLoginInformationResponseTO;
import org.projectX.dbo.profile.dao.ProfileDAO;
import org.projectX.dbo.profile.dao.request.InsertUserProfileRequestTO;
import org.projectX.dbo.profile.dao.request.InsertVerificationMethodRequestTO;
import org.projectX.dbo.profile.dao.response.InsertUserProfileResponseTO;
import org.projectX.dbo.profile.dao.response.InsertVerificationMethodResponseTO;
import org.projectX.dbo.utils.InterFace.ProjectX;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync
public final class RegistrationHelper {

	private Logger logger = LogManager.getLogger(RegistrationHelper.class);
	
	@Autowired
	private ProjectSUtils projectSUtils;
	
	@Autowired
	private ReportingDAO reportingDAO;
	
	@Autowired
	private ProfileDAO profileDAO;
	
	@Autowired
	private LoginDAO loginDAO;
	
	@Autowired
	private ProjectX projectX;
	
	public boolean registerHelper(RegisterProfileRequestTO registerProfileRequestTO,
			Future<RetrieveProjectSTrackingIdResponseTO> retrieveTrackingIdAsyncCall) throws ProjectSException {
		boolean registered=false;
		boolean validInput = false;
		String trckId = null;
		logger.info("Inside registerHelper");
		try{
			 if(registerProfileRequestTO!=null){
				 String email = registerProfileRequestTO.getEmail();
				 String firstName =  registerProfileRequestTO.getFirstName();
				 String lastName =  registerProfileRequestTO.getLastName();
				 String phoneNumber =  registerProfileRequestTO.getPhoneNumber();
				 String userName = registerProfileRequestTO.getUserName();
				 String password = registerProfileRequestTO.getPassword();
				 if(StringUtils.isNotBlank(firstName) && StringUtils.isNotBlank(lastName) 
						 && (StringUtils.isNotBlank(email)||StringUtils.isNotBlank(phoneNumber)) && StringUtils.isNotBlank(password)
						 && StringUtils.isNotBlank(userName)){
					 validInput = true;
					 Future<String> hashedPasswordAsyncCall  = projectSUtils.hashPassword(password);
					 Session session = projectX.getSessionFactoryInstance().openSession();
					 session.getTransaction().begin();
					 InsertUserProfileRequestTO insertUserProfileRequestTO = new InsertUserProfileRequestTO(firstName, lastName, phoneNumber, email);
					 insertUserProfileRequestTO.setEmployer(registerProfileRequestTO.getEmployer());
					 insertUserProfileRequestTO.setMiddleName(registerProfileRequestTO.getMiddleName());
					 insertUserProfileRequestTO.setNickName(registerProfileRequestTO.getNickName());
					 insertUserProfileRequestTO.setOccupationId(registerProfileRequestTO.getOccupationId());
					 if(StringUtils.isNotBlank(registerProfileRequestTO.getSex().toString())){
						 if(StringUtils.equalsIgnoreCase(registerProfileRequestTO.getSex().toString(), Sex.MALE.toString())){
							 insertUserProfileRequestTO.setSex(SexEnum.MALE);
						 }else{
							 insertUserProfileRequestTO.setSex(SexEnum.FEMALE);
						 }
					 }
					 InsertUserProfileResponseTO insertUserProfileResponseTO = profileDAO.insertUserProfile(insertUserProfileRequestTO, session);
					 if(insertUserProfileResponseTO!=null && insertUserProfileResponseTO.isInserted() &&
							 StringUtils.isNotBlank(insertUserProfileResponseTO.getUserProfileId())){
						 if(logger.isDebugEnabled())
							 logger.debug("inserUserProfile call ProjectXTrackingId::"+ insertUserProfileResponseTO.getProjXTrackingId());
						 String hasedPassword = projectSUtils.extractObject(hashedPasswordAsyncCall);
						 InsertLoginInformationRequestTO insertLoginInformationRequestTO = 
								 new InsertLoginInformationRequestTO(insertUserProfileResponseTO.getUserProfileId(), userName, hasedPassword);
						 InsertLoginInformationResponseTO insertLoginInformationResponseTO = loginDAO.insertLoginInformation
								 (insertLoginInformationRequestTO, session);
						 if(insertLoginInformationResponseTO!=null && insertLoginInformationResponseTO.isInserted() && 
								 StringUtils.isNotBlank(insertLoginInformationResponseTO.getUserId())){
							 if(logger.isDebugEnabled())
								 logger.debug("insertLoginInformation call ProjectXTrackingId::" + insertLoginInformationResponseTO.getProjXTrackId());
							 InsertLoginBiographyRequestTO insertLoginBiographyRequestTO = 
									 new InsertLoginBiographyRequestTO(insertLoginInformationResponseTO.getUserId());
							 InsertLoginBiographyResponseTO insertLoginBiographyResponseTO = loginDAO.insertLoginBiography(
									 insertLoginBiographyRequestTO, session);
							 if(insertLoginBiographyResponseTO!=null && insertLoginBiographyResponseTO.getLoginBiographyId()!=null
									 && insertLoginBiographyResponseTO.isInserted()){
								 if(logger.isDebugEnabled())
									 logger.debug("insertLoginBiography call ProjectXTrackingId::" + insertLoginBiographyResponseTO.getProjTrackId());
								 RegistrationVerificationTypeEnum registrationVerificationTypeEnum = null;
								 if(StringUtils.isNotBlank(phoneNumber) && StringUtils.isNotBlank(email)){
									 registrationVerificationTypeEnum = RegistrationVerificationTypeEnum.BOTH;
								 }else if(StringUtils.isNotBlank(phoneNumber)){
									 registrationVerificationTypeEnum = RegistrationVerificationTypeEnum.SMS;
								 }else if(StringUtils.isNotBlank(email)){
									 registrationVerificationTypeEnum = RegistrationVerificationTypeEnum.EMAIL;
								 }
								 InsertVerificationMethodRequestTO insertVerificationMethodRequestTO = new InsertVerificationMethodRequestTO(
										 insertUserProfileResponseTO.getUserProfileId(),registrationVerificationTypeEnum, projectSUtils.generateAGRN());
								 InsertVerificationMethodResponseTO insertVerificationMethodResponseTO =profileDAO.insertVerificationMethod(
										 insertVerificationMethodRequestTO, session);
								 if(insertVerificationMethodResponseTO!=null && insertVerificationMethodResponseTO.isInserted()){
									 if(logger.isDebugEnabled())
										 logger.debug("insertVerificationMethod call ProjectXTrackingId::" + insertVerificationMethodResponseTO.getProjXTrckId());
									 session.getTransaction().commit();
									 session.close();
									 registered = true;
								 }
							 }	 
						 }
					 } 
				 }
			 }
			 if(!validInput){
				 trckId  = projectSUtils.extracttrackingId((RetrieveProjectSTrackingIdResponseTO)
							projectSUtils.extractObject(retrieveTrackingIdAsyncCall));
				 ThrowProjectSExceptionRequestTO throwProjectSExceptionRequestTO = new 
							ThrowProjectSExceptionRequestTO(RegistrationConstant.PROJS_REG_100, trckId, RegistrationConstant.API);
				 throw reportingDAO.throwProjectSException(throwProjectSExceptionRequestTO);
			 }
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
		logger.info(retrieveTrackingIdAsyncCall);
		return registered;
	}

}
