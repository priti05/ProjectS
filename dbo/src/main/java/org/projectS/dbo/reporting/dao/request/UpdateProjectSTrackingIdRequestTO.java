package org.projectS.dbo.reporting.dao.request;

public final class UpdateProjectSTrackingIdRequestTO
{
  private String trackingId;
  private Object response;
  
  public UpdateProjectSTrackingIdRequestTO(String trackingId, Object response)
  {
    this.trackingId = trackingId;
    this.response = response;
  }
  
  public String getTrackingId()
  {
    return this.trackingId;
  }
  
  public Object getResponse()
  {
    return this.response;
  }
}
