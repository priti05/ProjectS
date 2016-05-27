package org.projectS.dbo.error.dto;

import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.annotations.Cascade;
import org.hibernate.validator.constraints.NotBlank;
import org.projectS.dbo.reporting.dto.PSE_Report;

@Entity
@Table(name="PSE")
public class PSE
{
  @Id
  @Column(name="ERROR_CODE", columnDefinition="varchar(100)")
  private String errorCode;
  @Column(name="ERROR_DESCRIPTION")
  @Lob
  @NotBlank(message="ERROR_DESCRIPTION in PSE can't be null or empty")
  private String errorDecription;
  @Column(name="MODULE", columnDefinition="varchar(100)")
  @NotBlank(message="MODULE cant be null or empty in PSE")
  private String module;
  @OneToMany(mappedBy="pse")
  @Cascade({org.hibernate.annotations.CascadeType.ALL})
  private Set<PSE_Report> PSE_ReportList;
  
  public PSE(String errorCode, String errorDecription, String module)
  {
    this.errorCode = errorCode;
    this.errorDecription = errorDecription;
    this.module = module;
  }
  
  public String getErrorCode()
  {
    return this.errorCode;
  }
  
  public void setErrorCode(String errorCode)
  {
    this.errorCode = errorCode;
  }
  
  public String getErrorDecription()
  {
    return this.errorDecription;
  }
  
  public void setErrorDecription(String errorDecription)
  {
    this.errorDecription = errorDecription;
  }
  
  public String getModule()
  {
    return this.module;
  }
  
  public void setModule(String module)
  {
    this.module = module;
  }
  
  public Set<PSE_Report> getPSE_ReportList()
  {
    return this.PSE_ReportList;
  }
  
  public void setPSE_ReportList(Set<PSE_Report> pSE_ReportList)
  {
    this.PSE_ReportList = pSE_ReportList;
  }
}
