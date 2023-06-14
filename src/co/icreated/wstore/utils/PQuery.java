package co.icreated.wstore.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

import org.adempiere.exceptions.AdempiereException;
import org.adempiere.exceptions.DBException;
import org.compiere.model.MTable;
import org.compiere.model.PO;
import org.compiere.model.Query;
import org.compiere.util.CLogger;
import org.compiere.util.DB;

/**
 * This class replaces Idempiere Query. Actually, custom Idempiere ctx not propagated correctly and
 * result methods "first", "firstOnly", "list" loose it, because of calls of Env.getCtx() on low
 * level The unique solution is extend Query & fix propagation error.
 */
public class PQuery extends Query {

  CLogger log = CLogger.getCLogger(PQuery.class);

  Properties ctx = null;
  MTable table = null;
  String trxName = null;


  public PQuery(Properties ctx, String tableName, String whereClause, String trxName) {
    super(ctx, tableName, whereClause, trxName);
    this.ctx = ctx;
    this.table = MTable.get(ctx, tableName);
    this.trxName = trxName;
  }


  @Override
  public <T extends PO> T first() throws DBException {

    T t = super.first();
    if (t == null) {
      return null;
    }
    // Fix missing / lost context
    t.getCtx().putAll(ctx);
    return t;
  }


  @Override
  public <T extends PO> T firstOnly() throws DBException {

    T t = super.firstOnly();
    if (t == null) {
      return null;
    }
    // Fix missing / lost context
    t.getCtx().putAll(ctx);
    return t;
  }


  @Override
  @SuppressWarnings("unchecked")
  public <T extends PO> List<T> list() throws DBException {
    List<T> list = new ArrayList<T>();
    String sql = buildSqlFix(null, true);

    PreparedStatement pstmt = null;
    ResultSet rs = null;
    try {
      pstmt = DB.prepareStatement(sql, trxName);
      rs = createResultSetFix(pstmt);
      while (rs.next()) {
        T po = (T) table.getPO(rs, trxName);
        // Fix missing / lost context in Query
        po.getCtx().putAll(ctx);
        list.add(po);
      }
    } catch (SQLException e) {
      log.log(Level.SEVERE, sql, e);
      throw new DBException(e, sql);
    } finally {
      DB.close(rs, pstmt);
      rs = null;
      pstmt = null;
    }
    return list;
  }


  /**
   * The buildSQL is private method. Workaround is calling it by reflection
   *
   * @param selectClause
   * @param useOrderByClause
   * @return
   */
  private String buildSqlFix(StringBuilder selectClause, boolean useOrderByClause) {
    try {
      Method buildSql =
          Query.class.getDeclaredMethod("buildSQL", StringBuilder.class, boolean.class);
      buildSql.setAccessible(true);
      return (String) buildSql.invoke(this, selectClause, useOrderByClause);

    } catch (NoSuchMethodException | SecurityException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IllegalArgumentException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    throw new AdempiereException("No SQL");
  }


  /**
   * The createResultSet is private method Workaround is calling it by reflection
   *
   * @param pstmt
   * @return
   */
  public final ResultSet createResultSetFix(PreparedStatement pstmt) {
    try {
      Method buildSql = Query.class.getDeclaredMethod("createResultSet", PreparedStatement.class);
      buildSql.setAccessible(true);
      return (ResultSet) buildSql.invoke(this, pstmt);

    } catch (NoSuchMethodException | SecurityException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IllegalArgumentException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    throw new AdempiereException("No ResultSet");
  }

}
