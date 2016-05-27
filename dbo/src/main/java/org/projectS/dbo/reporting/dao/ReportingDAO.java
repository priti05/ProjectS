package org.projectS.dbo.reporting.dao;

import java.util.concurrent.Future;
import org.projectS.dbo.error.exception.ProjectSException;
import org.projectS.dbo.reporting.dao.request.RetrieveProjectSTrackingIdRequestTO;
import org.projectS.dbo.reporting.dao.request.ThrowProjectSExceptionRequestTO;
import org.projectS.dbo.reporting.dao.request.UpdateProjectSTrackingIdRequestTO;
import org.projectS.dbo.reporting.dao.response.RetrieveProjectSTrackingIdResponseTO;
import org.springframework.scheduling.annotation.Async;

public abstract interface ReportingDAO
{
  @Async
  public abstract Future<RetrieveProjectSTrackingIdResponseTO> retrieveTrackingId(RetrieveProjectSTrackingIdRequestTO paramRetrieveProjectSTrackingIdRequestTO);
  
  @Async
  public abstract void updateTrackingIdTable(UpdateProjectSTrackingIdRequestTO paramUpdateProjectSTrackingIdRequestTO);
  
  public abstract ProjectSException throwProjectSException(ThrowProjectSExceptionRequestTO paramThrowProjectSExceptionRequestTO);
}
