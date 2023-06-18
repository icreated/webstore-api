package co.icreated.wstore.mapper;

import org.compiere.model.MUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import co.icreated.wstore.api.model.AccountInfoDto;


@Mapper
public interface AccountMapper {

  public AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);


  @Mapping(source = "aD_User_ID", target = "id")
  public AccountInfoDto toDto(MUser user);


}
