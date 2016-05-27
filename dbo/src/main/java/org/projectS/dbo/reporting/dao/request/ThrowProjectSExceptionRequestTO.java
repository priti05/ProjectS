package org.projectS.dbo.reporting.dao.request;

public final class ThrowProjectSExceptionRequestTO
{
  private String errorCode;
  private String trckId;
  private String api;
  
  public ThrowProjectSExceptionRequestTO(String errorCode, String trckId, String api)
  {
    this.errorCode = errorCode;
    this.trckId = trckId;
    this.api = api;
  }
  
  public String getErrorCode()
  {
    return this.errorCode;
  }
  
  public String getTrckId()
  {
    return this.trckId;
  }
  
  public String getApi()
  {
    return this.api;
  }
}
