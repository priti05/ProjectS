package org.projectS.dbo.reporting.dto;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import org.projectS.dbo.error.dto.PSE;

@Entity
@Table(name="PSE_REPORT")
public class PSE_Report
{
  @Id
  @GeneratedValue(strategy=GenerationType.AUTO)
  @Column(name="PSE_REPORT_ID")
  private long PSE_Report_Id;
  @ManyToOne
  @JoinColumn(name="ERROR_CODE", foreignKey=@ForeignKey(name="PSE_FK_PSE_REPORT"))
  @NotNull(message="ERROR_CODE can't be null in PSE_REPORT")
  private PSE pse;
  @Column(name="REPORTED_ON")
  @NotNull(message="REPORTED_ON can't be null in PSE_REPORT")
  @Temporal(TemporalType.TIMESTAMP)
  private Date reportedOn;
  @OneToOne
  @JoinColumn(name="PROJECT_S_TRACKING_ID", foreignKey=@ForeignKey(name="PROJ_S_TRCK_FK_PSE_RPRT"))
  @NotNull(message="PROJECT_S_TRACKING_ID can't be null in PSE_REPORT")
  private ProjectSTrackingIdGeneratorTracker projectsTrackingIdGeneratorTracker;
  
  public PSE_Report(PSE pse, Date reportedOn, ProjectSTrackingIdGeneratorTracker projectsTrackingIdGeneratorTracker)
  {
    this.pse = pse;
    this.reportedOn = reportedOn;
    this.projectsTrackingIdGeneratorTracker = projectsTrackingIdGeneratorTracker;
  }
  
  public long getPSE_Report_Id()
  {
    return this.PSE_Report_Id;
  }
  
  public void setPSE_Report_Id(long pSE_Report_Id)
  {
    this.PSE_Report_Id = pSE_Report_Id;
  }
  
  public PSE getPse()
  {
    return this.pse;
  }
  
  public void setPse(PSE pse)
  {
    this.pse = pse;
  }
  
  public Date getReportedOn()
  {
    return this.reportedOn;
  }
  
  public void setReportedOn(Date reportedOn)
  {
    this.reportedOn = reportedOn;
  }
  
  public ProjectSTrackingIdGeneratorTracker getProjectsTrackingIdGeneratorTracker()
  {
    return this.projectsTrackingIdGeneratorTracker;
  }
  
  public void setProjectsTrackingIdGeneratorTracker(ProjectSTrackingIdGeneratorTracker projectsTrackingIdGeneratorTracker)
  {
    this.projectsTrackingIdGeneratorTracker = projectsTrackingIdGeneratorTracker;
  }
}
