package org.projectS.dbo.reporting.dao.request;

public final class RetrieveProjectSTrackingIdRequestTO
{
  private Object request;
  private String api;
  
  public RetrieveProjectSTrackingIdRequestTO(Object request, String api)
  {
    this.request = request;
    this.api = api;
  }
  
  public Object getRequest()
  {
    return this.request;
  }
  
  public String getApi()
  {
    return this.api;
  }
}
