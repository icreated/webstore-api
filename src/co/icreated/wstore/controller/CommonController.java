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
package co.icreated.wstore.controller;

import java.util.List;
import java.util.Properties;

import javax.annotation.security.PermitAll;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.core.Context;

import co.icreated.wstore.api.model.IdNameBeanDto;
import co.icreated.wstore.api.model.ShipperDto;
import co.icreated.wstore.api.model.StatusDto;
import co.icreated.wstore.api.model.StatusDto.StatusEnum;
import co.icreated.wstore.api.model.TokenDto;
import co.icreated.wstore.api.service.CommonApi;
import co.icreated.wstore.service.CommonService;


@PermitAll
public class CommonController implements CommonApi {

  @Context
  Properties ctx;

  @Context
  CommonService commonService;


  @Override
  public List<IdNameBeanDto> getCountries() {
    return commonService.getCountries();
  }

  @Override
  public List<ShipperDto> getShippers() {
    return commonService.getShippers();
  }

  @Override
  public StatusDto lookupEmail(@Valid @NotNull TokenDto tokenDto) {
    boolean ok = commonService.isUnique(ctx, "Email", tokenDto.getToken());
    return new StatusDto().status(ok ? StatusEnum.OK : StatusEnum.NOT_FOUND);
  }


}
