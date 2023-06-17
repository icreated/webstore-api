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


import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import org.compiere.model.MCountry;
import org.compiere.model.MShipper;
import org.compiere.model.MUser;
import org.compiere.util.CLogger;

import co.icreated.wstore.api.model.IdNameBeanDto;
import co.icreated.wstore.api.model.ShipperDto;
import co.icreated.wstore.mapper.CommonMapper;
import co.icreated.wstore.utils.PQuery;

public class CommonService {


  private static CLogger log = CLogger.getCLogger(CommonService.class);
  Properties ctx;


  public CommonService(Properties ctx) {
    this.ctx = ctx;
  }


  public List<ShipperDto> getShippers() {

    return new PQuery(ctx, MShipper.Table_Name, "", null) //
        .setClient_ID() //
        .setOnlyActiveRecords(true) //
        .setOrderBy("Name") //
        .<MShipper>stream() //
        .map(CommonMapper.INSTANCE::toDto) //
        .collect(Collectors.toList());
  }


  public boolean isUnique(Properties ctx, String field, String token) {

    if (token == null)
      return false;

    return new PQuery(ctx, MUser.Table_Name, "UPPER(%s)=?", null) //
        .setClient_ID() //
        .setOnlyActiveRecords(true) //
        .setParameters(token.toUpperCase()) //
        .match();
  }


  public List<IdNameBeanDto> getCountries() {

    return new PQuery(ctx, MCountry.Table_Name, "", null) //
        .setClient_ID() //
        .setOnlyActiveRecords(true) //
        .setOrderBy("Name") //
        .<MCountry>stream() //
        .map(CommonMapper.INSTANCE::toDto) //
        .collect(Collectors.toList());
  }
}

