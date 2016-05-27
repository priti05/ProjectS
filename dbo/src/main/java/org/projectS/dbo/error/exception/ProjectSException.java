package org.projectS.dbo.error.exception;

public class ProjectSException
  extends Exception
{
  private static final long serialVersionUID = -5769882202738223381L;
  private String apiName;
  private String projectSTrackingId;
  private String errorCode;
  private String errorDescription;
  
  public ProjectSException(String apiName, String projectSTrackingId, String errorCode, String errorDescription)
  {
    this.apiName = apiName;
    this.projectSTrackingId = projectSTrackingId;
    this.errorCode = errorCode;
    this.errorDescription = errorDescription;
  }
  
  public String getApiName()
  {
    return this.apiName;
  }
  
  public String getProjectSTrackingId()
  {
    return this.projectSTrackingId;
  }
  
  public String getErrorCode()
  {
    return this.errorCode;
  }
  
  public String getErrorDescription()
  {
    return this.errorDescription;
  }
}
