package co.icreated.wstore.utils;

import java.util.function.Function;

import javax.ws.rs.WebApplicationException;

import org.compiere.util.CLogger;
import org.compiere.util.Trx;

import co.icreated.wstore.exception.WstoreInternalServerException;


public class Transaction {

  static CLogger log = CLogger.getCLogger(Transaction.class);

  public static <T> T run(Function<String, T> transaction) {

    Trx trx = null;
    T instance = null;
    try {
      String transactionName = Thread.currentThread().getStackTrace()[2].getMethodName();
      String trxName = Trx.createTrxName(transactionName);
      trx = Trx.get(trxName, true);
      instance = transaction.apply(trxName);
      if (trx.commit()) {
        return instance;
      }

    } catch (Throwable e) {
      if (trx != null) {
        trx.rollback();
        trx.close();
        trx = null;
        throw new WstoreInternalServerException("Transaction error: " + e.getMessage());
      }
    } finally {
      if (trx != null) {
        trx.close();
      }
    }
    return null;
  }

}
