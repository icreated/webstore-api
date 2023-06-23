package co.icreated.wstore.utils;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.logging.Level;

import org.compiere.model.MUser;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Trx;

public class Utils {
	
	static CLogger log = CLogger.getCLogger(Utils.class); 
	
 public static <T> T nativeFirstQuery(String sql, Map<Integer, Object> params, ThrowingFunction<ResultSet, T, Exception> function) {
	 
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		T retValue = null;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			applyParams(pstmt, params);
			rs = pstmt.executeQuery();
			if (rs.next())
			{
				try {
					retValue = function.apply(rs);
				} catch(SQLException esql) {
					
				}

			}
			else
				log.fine("No record");
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}
		return retValue;
 }	
 
 
 private static void applyParams(PreparedStatement pstmt, Map<Integer, Object> params) {
	 params.forEach((index, param) -> {
			 try {
				 if (param instanceof String) {
					 pstmt.setString(index, (String)param);
				 } else if (param instanceof Integer) {
					 pstmt.setInt(index, (Integer)param);
				 } else if (param instanceof Timestamp) {
					 pstmt.setTimestamp(index, (Timestamp)param);
				 } else if (param instanceof BigDecimal) {
					 pstmt.setBigDecimal(index, (BigDecimal)param);
				 } else {
					 log.log(Level.SEVERE, "Instance param not implemented");
				 }
			} catch (SQLException e) {
				log.log(Level.SEVERE, "Error applying param: ", e);
			}

	 });
 }

 
  public static <T> T trx(Function<String, T> transaction) {

    Trx trx = null;
    try {
      String transactionName = Thread.currentThread().getStackTrace()[2].getMethodName();
      String trxName = Trx.createTrxName(transactionName);
      trx = Trx.get(trxName, true);
      T object = transaction.apply(trxName);
      if (trx.commit()) {
        return object;
      }

    } catch (Throwable e) {
      if (trx != null) {
        trx.rollback();
        trx.close();
        trx = null;
      }
    } finally {
      if (trx != null) {
        trx.close();
      }
    }
    return null;
  }

}
