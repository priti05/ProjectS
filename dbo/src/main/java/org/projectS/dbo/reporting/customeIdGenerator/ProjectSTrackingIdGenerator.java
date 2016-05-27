package org.projectS.dbo.reporting.customeIdGenerator;

import java.io.Serializable;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.IdentifierGenerator;

public class ProjectSTrackingIdGenerator
  implements IdentifierGenerator
{
  //private final String PROJECTSTRCKID_SEQUENCE = "ProjectSTrckId_Sequence";
  private Logger logger = LogManager.getLogger(ProjectSTrackingIdGenerator.class);
  
  public Serializable generate(SessionImplementor session, Object object)
    throws HibernateException
  {
    this.logger.info("Inside generate");
    Serializable suffix = null;
    String projectS = "PROJECT_S_";
    Connection connection = null;
    Statement statement = null;
    ResultSet resultSet = null;
    try
    {
      BigInteger prefix = new BigInteger("10000000");
      connection = session.connection();
      statement = connection.createStatement();
      try
      {
        String exe = "SELECT ProjectSTrckId_Sequence.nextval FROM DUAL";
        resultSet = statement.executeQuery(exe);
      }
      catch (Exception ex)
      {
        this.logger.error("stack trace::", ex);
        statement = connection.createStatement();
        statement.execute("CREATE SEQUENCE ProjectSTrckId_Sequence START WITH 1 MAXVALUE 999999999999999999999999999 INCREMENT BY 1 CYCLE");
        this.logger.warn("CREATE SEQUENCE ProjectSTrckId_Sequence START WITH 1 MAXVALUE 999999999999999999999999999 INCREMENT BY 1 CYCLE");
        resultSet = statement.executeQuery("SELECT ProjectSTrckId_Sequence.nextval FROM DUAL");
      }
      if (resultSet.next())
      {
        DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        suffix = projectS + prefix.add(new BigInteger(String.valueOf(resultSet.getObject(1)))) + df.format(new Date(System.currentTimeMillis()));
      }
    }
    catch (SQLException e)
    {
      this.logger.error("Exception at generate::", new Object[] { e.getMessage() });
      this.logger.error("stack Traces::", e);
    }
    if (this.logger.isDebugEnabled()) {
      this.logger.debug("generated PROJECT_S_TRK_ID::" + suffix.toString());
    }
    this.logger.info("End of generate");
    return suffix;
  }
}
