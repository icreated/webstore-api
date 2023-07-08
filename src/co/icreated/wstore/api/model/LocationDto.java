package co.icreated.wstore.api.model;

import co.icreated.wstore.api.model.CountryDto;
import com.fasterxml.jackson.annotation.JsonTypeName;
import javax.validation.constraints.*;
import javax.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;



@JsonTypeName("Location")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")
public class LocationDto   {
  private @Valid Integer id;
  private @Valid String address1;
  private @Valid String address2;
  private @Valid String postal;
  private @Valid String city;
  private @Valid CountryDto country;

  /**
   **/
  public LocationDto id(Integer id) {
    this.id = id;
    return this;
  }

  
  @JsonProperty("id")
  public Integer getId() {
    return id;
  }

  @JsonProperty("id")
  public void setId(Integer id) {
    this.id = id;
  }

  /**
   **/
  public LocationDto address1(String address1) {
    this.address1 = address1;
    return this;
  }

  
  @JsonProperty("address1")
  @NotNull
  public String getAddress1() {
    return address1;
  }

  @JsonProperty("address1")
  public void setAddress1(String address1) {
    this.address1 = address1;
  }

  /**
   **/
  public LocationDto address2(String address2) {
    this.address2 = address2;
    return this;
  }

  
  @JsonProperty("address2")
  public String getAddress2() {
    return address2;
  }

  @JsonProperty("address2")
  public void setAddress2(String address2) {
    this.address2 = address2;
  }

  /**
   **/
  public LocationDto postal(String postal) {
    this.postal = postal;
    return this;
  }

  
  @JsonProperty("postal")
  @NotNull
  public String getPostal() {
    return postal;
  }

  @JsonProperty("postal")
  public void setPostal(String postal) {
    this.postal = postal;
  }

  /**
   **/
  public LocationDto city(String city) {
    this.city = city;
    return this;
  }

  
  @JsonProperty("city")
  @NotNull
  public String getCity() {
    return city;
  }

  @JsonProperty("city")
  public void setCity(String city) {
    this.city = city;
  }

  /**
   **/
  public LocationDto country(CountryDto country) {
    this.country = country;
    return this;
  }

  
  @JsonProperty("country")
  @NotNull
  public CountryDto getCountry() {
    return country;
  }

  @JsonProperty("country")
  public void setCountry(CountryDto country) {
    this.country = country;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LocationDto location = (LocationDto) o;
    return Objects.equals(this.id, location.id) &&
        Objects.equals(this.address1, location.address1) &&
        Objects.equals(this.address2, location.address2) &&
        Objects.equals(this.postal, location.postal) &&
        Objects.equals(this.city, location.city) &&
        Objects.equals(this.country, location.country);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, address1, address2, postal, city, country);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LocationDto {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    address1: ").append(toIndentedString(address1)).append("\n");
    sb.append("    address2: ").append(toIndentedString(address2)).append("\n");
    sb.append("    postal: ").append(toIndentedString(postal)).append("\n");
    sb.append("    city: ").append(toIndentedString(city)).append("\n");
    sb.append("    country: ").append(toIndentedString(country)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }


}

