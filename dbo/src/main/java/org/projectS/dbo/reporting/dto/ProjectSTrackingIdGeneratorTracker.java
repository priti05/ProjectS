package org.projectS.dbo.reporting.dto;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="PROJECT_S_TRACKING_ID_TRACKER")
public class ProjectSTrackingIdGeneratorTracker
  implements Comparable<ProjectSTrackingIdGeneratorTracker>
{
  @Id
  @GenericGenerator(name="tracking_id", strategy="org.projectS.dbo.reporting.customeIdGenerator.ProjectSTrackingIdGenerator")
  @GeneratedValue(generator="tracking_id")
  @Column(name="PROJECT_S_TRACKING_ID", columnDefinition="varchar(300)")
  private String trackingId;
  @Lob
  @Column(name="REQUEST")
  @NotNull(message="REQUEST can't be null in PROJECT_S_TRACKING_ID_TRACKER")
  private String request;
  @Lob
  @Column(name="RESPONSE")
  private String response;
  @Column(name="REQUEST_DATE")
  @NotNull(message="REQUEST_DATE can't be null in PROJECT_S_TRACKING_ID_TRACKER")
  @Temporal(TemporalType.TIMESTAMP)
  private Date requestDate;
  @Column(name="API_NAME")
  @NotNull(message="API_NAME can't be null in PROJECT_S_TRACKING_ID_TRACKER")
  private String apiName;
  @OneToOne(mappedBy="projectSTrackingIdGeneratorTracker")
  @Cascade({org.hibernate.annotations.CascadeType.ALL})
  private PSE_Report pse_report;
  
  public ProjectSTrackingIdGeneratorTracker(String request, Date requestDate, String apiName)
  {
    this.request = request;
    this.requestDate = requestDate;
    this.apiName = apiName;
  }
  
  public String getTrackingId()
  {
    return this.trackingId;
  }
  
  public void setTrackingId(String trackingId)
  {
    this.trackingId = trackingId;
  }
  
  public String getRequest()
  {
    return this.request;
  }
  
  public void setRequest(String request)
  {
    this.request = request;
  }
  
  public String getResponse()
  {
    return this.response;
  }
  
  public void setResponse(String response)
  {
    this.response = response;
  }
  
  public Date getRequestDate()
  {
    return this.requestDate;
  }
  
  public void setRequestDate(Date requestDate)
  {
    this.requestDate = requestDate;
  }
  
  public String getApiName()
  {
    return this.apiName;
  }
  
  public void setApiName(String apiName)
  {
    this.apiName = apiName;
  }
  
  public PSE_Report getPse_report()
  {
    return this.pse_report;
  }
  
  public void setPse_report(PSE_Report pse_report)
  {
    this.pse_report = pse_report;
  }
  
  public int compareTo(ProjectSTrackingIdGeneratorTracker pstigt)
  {
    Date reqDate = pstigt.getRequestDate();
    Date currentReqDate = getRequestDate();
    return reqDate.compareTo(currentReqDate);
  }
}
