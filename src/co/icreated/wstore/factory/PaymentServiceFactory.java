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
package co.icreated.wstore.factory;

import java.util.Properties;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

import co.icreated.wstore.model.SessionUser;
import co.icreated.wstore.service.PaymentService;

public class PaymentServiceFactory extends AbstractServiceFactory<PaymentService> {

  @Inject
  public PaymentServiceFactory(@Context ContainerRequestContext context,
      @Context SecurityContext securityContext, @Context Properties ctx) {
    super(context, "paymentService", () -> {
      return new PaymentService(ctx, securityContext);
    });
  }
}
