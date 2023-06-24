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
  public void lookupEmail(@Valid @NotNull TokenDto tokenDto) {
    commonService.isUnique(ctx, "Email", tokenDto.getToken());
  }


}
