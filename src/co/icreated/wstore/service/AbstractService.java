/*******************************************************************************
 * @author Copyright (C) 2019 ICreated, Sergey Polyarus
 * @date 2019 This program is free software; you can redistribute it and/or modify it under the
 *       terms version 2 of the GNU General Public License as published by the Free Software
 *       Foundation. This program is distributed in the hope that it will be useful, but WITHOUT ANY
 *       WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 *       PURPOSE. See the GNU General Public License for more details. You should have received a
 *       copy of the GNU General Public License along with this program; if not, write to the Free
 *       Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.
 ******************************************************************************/
package co.icreated.wstore.service;

import java.util.Properties;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

import org.compiere.util.Trx;


import co.icreated.wstore.model.SessionUser;

public abstract class AbstractService {

  Properties ctx = null;
  private SecurityContext securityContext;

  protected AbstractService(Properties ctx) {
    this.ctx = ctx;
  }

  AbstractService(Properties ctx, SecurityContext securityContext) {
    this.ctx = ctx;
    this.securityContext = securityContext;
  }


  protected SessionUser getSessionUser() {
    return (SessionUser) securityContext.getUserPrincipal();
  }


  public <T> T transaction(Function<String, T> transaction) {

    String callingMethodName = Thread.currentThread().getStackTrace()[2].getMethodName();
    String trxName = Trx.createTrxName(callingMethodName);
    Trx trx = Trx.get(trxName, true);
    T object = transaction.apply(trxName);
    if (trx.commit()) {
      return object;
    } else {
      trx.rollback();
      throw new InternalServerErrorException("Transaction not commited: " + trxName);
    }
  }
}
