package co.icreated.wstore.service;

import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import org.compiere.model.MCountry;
import org.compiere.model.MShipper;
import org.compiere.model.MUser;

import co.icreated.wstore.api.model.IdNameBeanDto;
import co.icreated.wstore.api.model.ShipperDto;
import co.icreated.wstore.mapper.CommonMapper;
import co.icreated.wstore.utils.PQuery;

public class CommonService extends AbstractService {


  public CommonService(Properties ctx) {
    super(ctx);
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
        .setOnlyActiveRecords(true) //
        .setOrderBy("Name") //
        .<MCountry>stream() //
        .map(CommonMapper.INSTANCE::toDto) //
        .collect(Collectors.toList());
  }
}

