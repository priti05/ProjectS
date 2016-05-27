package org.projectS.dbo.reporting.dao.response;

public final class RetrieveProjectSTrackingIdResponseTO
{
  private String trackingId;
  
  public RetrieveProjectSTrackingIdResponseTO(String trackingId)
  {
    this.trackingId = trackingId;
  }
  
  public String getTrackingId()
  {
    return this.trackingId;
  }
}
