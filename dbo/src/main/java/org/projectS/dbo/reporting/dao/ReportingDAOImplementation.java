package org.projectS.dbo.reporting.dao;

import com.google.gson.Gson;
import java.util.Date;
import java.util.concurrent.Future;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.projectS.dbo.error.dto.PSE;
import org.projectS.dbo.error.exception.ProjectSException;
import org.projectS.dbo.reporting.dao.request.RetrieveProjectSTrackingIdRequestTO;
import org.projectS.dbo.reporting.dao.request.ThrowProjectSExceptionRequestTO;
import org.projectS.dbo.reporting.dao.request.UpdateProjectSTrackingIdRequestTO;
import org.projectS.dbo.reporting.dao.response.RetrieveProjectSTrackingIdResponseTO;
import org.projectS.dbo.reporting.dto.PSE_Report;
import org.projectS.dbo.reporting.dto.ProjectSTrackingIdGeneratorTracker;
import org.projectS.dbo.util.ProjectSHibernateSpringFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;

final class ReportingDAOImplementation
  implements ReportingDAO
{
  private Logger logger = LogManager.getLogger(ReportingDAOImplementation.class);
  @Autowired
  private ProjectSHibernateSpringFactory projectSHibernateSpringFactory;
  
  @Async
  public Future<RetrieveProjectSTrackingIdResponseTO> retrieveTrackingId(RetrieveProjectSTrackingIdRequestTO retrieveProjectSTrackingIdRequestTO)
  {
    this.logger.info("Inside retrieveTrackingId");
    RetrieveProjectSTrackingIdResponseTO retrieveProjectSTrackingIdResponseTO = null;
    if ((retrieveProjectSTrackingIdRequestTO != null) && (retrieveProjectSTrackingIdRequestTO.getRequest() != null) && 
      (StringUtils.isNotBlank(retrieveProjectSTrackingIdRequestTO.getApi())))
    {
      String jsonRequest = convertObJTOJson(retrieveProjectSTrackingIdRequestTO.getRequest());
      ProjectSTrackingIdGeneratorTracker projectSTrackingIdGeneratorTracker = new ProjectSTrackingIdGeneratorTracker(jsonRequest, 
        new Date(), retrieveProjectSTrackingIdRequestTO.getApi());
      SessionFactory sessionFactory = this.projectSHibernateSpringFactory.getSessionFactoryInstance();
      Session session = sessionFactory.openSession();
      session.getTransaction().begin();
      String trackingId = (String)session.save(projectSTrackingIdGeneratorTracker);
      if (this.logger.isDebugEnabled())
      {
        this.logger.debug(trackingId);
        this.logger.debug(jsonRequest);
      }
      session.getTransaction().commit();
      session.close();
      retrieveProjectSTrackingIdResponseTO = new RetrieveProjectSTrackingIdResponseTO(trackingId);
    }
    this.logger.info("End of retrieveTrackingId");
    return new AsyncResult<RetrieveProjectSTrackingIdResponseTO>(retrieveProjectSTrackingIdResponseTO);
  }
  
  private String convertObJTOJson(Object response)
  {
    String jsonResponse = "response has null value";
    if (response != null)
    {
      Gson gson = new Gson();
      jsonResponse = gson.toJson(response);
    }
    this.logger.info("Object to JSON::" + jsonResponse);
    return jsonResponse;
  }
  
  private void updateTrackingIdTableWithPSE(Session session, String TrackingId, ProjectSException response, PSE pse)
  {
    this.logger.info("Inside updateTrackingIdTableWithPSE");
    if (StringUtils.isNotBlank(TrackingId))
    {
      ProjectSTrackingIdGeneratorTracker projectSTrackingIdGeneratorTracker = (ProjectSTrackingIdGeneratorTracker)
        session.get(ProjectSTrackingIdGeneratorTracker.class, TrackingId);
      PSE_Report pse_Report = new PSE_Report(pse, new Date(), projectSTrackingIdGeneratorTracker);
      projectSTrackingIdGeneratorTracker.setPse_report(pse_Report);
      projectSTrackingIdGeneratorTracker.setResponse(convertObJTOJson(response));
      session.update(projectSTrackingIdGeneratorTracker);
      session.getTransaction().commit();
      session.close();
    }
    this.logger.info("End of updateTrackingIdTableWithPSE");
  }
  
  @Async
  public void updateTrackingIdTable(UpdateProjectSTrackingIdRequestTO updateProjectSTrackingIdRequestTO)
  {
    this.logger.info("Inside updateTrackingIdTable");
    if ((updateProjectSTrackingIdRequestTO != null) && 
      (StringUtils.isNotBlank(updateProjectSTrackingIdRequestTO.getTrackingId())))
    {
      SessionFactory sessionFactory = this.projectSHibernateSpringFactory.getSessionFactoryInstance();
      Session session = sessionFactory.openSession();
      session.getTransaction().begin();
      ProjectSTrackingIdGeneratorTracker projectSTrackingIdGeneratorTracker = (ProjectSTrackingIdGeneratorTracker)
        session.get(ProjectSTrackingIdGeneratorTracker.class, updateProjectSTrackingIdRequestTO.getTrackingId());
      projectSTrackingIdGeneratorTracker.setResponse(convertObJTOJson(updateProjectSTrackingIdRequestTO.getResponse()));
      session.update(projectSTrackingIdGeneratorTracker);
      session.getTransaction().commit();
      session.close();
    }
    this.logger.info("End of updateTrackingIdTable");
  }
  
  public ProjectSException throwProjectSException(ThrowProjectSExceptionRequestTO throwProjectSExceptionRequestTO)
  {
    this.logger.info("Inside throwProjectSException");
    ProjectSException projectSException = null;
    SessionFactory sessionFactory = this.projectSHibernateSpringFactory.getSessionFactoryInstance();
    Session session = sessionFactory.openSession();
    session.getTransaction().begin();
    try
    {
      this.logger.info(convertObJTOJson(throwProjectSExceptionRequestTO));
      if ((throwProjectSExceptionRequestTO != null) && (StringUtils.isNotBlank(throwProjectSExceptionRequestTO.getErrorCode())))
      {
        PSE pse = (PSE)session.get(PSE.class, throwProjectSExceptionRequestTO.getErrorCode());
        projectSException = new ProjectSException(throwProjectSExceptionRequestTO.getApi(), throwProjectSExceptionRequestTO.getTrckId(), 
          throwProjectSExceptionRequestTO.getErrorCode(), pse.getErrorDecription());
        updateTrackingIdTableWithPSE(session, throwProjectSExceptionRequestTO.getTrckId(), projectSException, pse);
      }
    }
    catch (Exception e)
    {
      this.logger.error("Exception at genericError", new Object[] { e.getMessage() });
      this.logger.error("stack traces::", e);
      PSE pse = new PSE("PROJS_ERROR_CODE_NOT_FOUND", "Looks like There was a error and error code not found in DB, Please check withProjectS DBO team with TrackingID", 
        "Not found");
      projectSException = new ProjectSException(throwProjectSExceptionRequestTO.getApi(), 
        throwProjectSExceptionRequestTO.getTrckId(), "PROJS_ERROR_CODE_NOT_FOUND", 
        throwProjectSExceptionRequestTO.getErrorCode() + " not found in DB, Please enter");
      updateTrackingIdTableWithPSE(session, throwProjectSExceptionRequestTO.getTrckId(), projectSException, pse);
    }
    this.logger.info("End of throwProjectSException");
    return projectSException;
  }
}
