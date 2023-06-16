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

import co.icreated.wstore.bean.SessionUser;

public abstract class AbstractService {


  Properties ctx = null;
  SessionUser sessionUser;

  AbstractService() {}

  AbstractService(Properties ctx) {

    this.ctx = ctx;
  }

  AbstractService(Properties ctx, SessionUser sessionUser) {

    this.ctx = ctx;
    this.sessionUser = sessionUser;
  }

}
