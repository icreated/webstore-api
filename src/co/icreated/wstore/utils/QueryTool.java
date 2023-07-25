package co.icreated.wstore.utils;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Map;
import java.util.logging.Level;

import org.compiere.util.CLogger;
import org.compiere.util.DB;

public class QueryTool {

  static CLogger log = CLogger.getCLogger(QueryTool.class);

  public static <T> T nativeFirst(String sql, Map<Integer, Object> params,
      ThrowingFunction<ResultSet, T, Exception> function) {

    PreparedStatement pstmt = null;
    ResultSet rs = null;
    T retValue = null;
    try {
      pstmt = DB.prepareStatement(sql, null);
      applyParams(pstmt, params);
      rs = pstmt.executeQuery();
      if (rs.next()) {
        retValue = function.apply(rs);
      } else
        log.fine("No record");
    } catch (Exception e) {
      log.log(Level.SEVERE, sql, e);
    } finally {
      DB.close(rs, pstmt);
      rs = null;
      pstmt = null;
    }
    return retValue;
  }


  private static void applyParams(PreparedStatement pstmt, Map<Integer, Object> params) {
    params.forEach((index, param) -> {
      try {
        if (param instanceof String) {
          pstmt.setString(index, (String) param);
        } else if (param instanceof Integer) {
          pstmt.setInt(index, (Integer) param);
        } else if (param instanceof Timestamp) {
          pstmt.setTimestamp(index, (Timestamp) param);
        } else if (param instanceof BigDecimal) {
          pstmt.setBigDecimal(index, (BigDecimal) param);
        } else {
          log.log(Level.SEVERE, "Instance param not implemented");
        }
      } catch (SQLException e) {
        log.log(Level.SEVERE, "Error applying param: ", e);
      }

    });
  }



}
