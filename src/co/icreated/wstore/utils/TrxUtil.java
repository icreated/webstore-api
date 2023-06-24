package co.icreated.wstore.utils;

import java.util.function.Function;

import org.compiere.util.CLogger;
import org.compiere.util.Trx;

public class TrxUtil {

  static CLogger log = CLogger.getCLogger(TrxUtil.class);

  public static <T> T commit(Function<String, T> transaction) {

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
