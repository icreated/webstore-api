package co.icreated.wstore.api.model;

import com.fasterxml.jackson.annotation.JsonTypeName;
import javax.validation.constraints.*;
import javax.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;



@JsonTypeName("Address")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")
public class AddressDto {
  private @Valid Integer id;
  private @Valid String label;
  private @Valid String name;
  private @Valid String address1;
  private @Valid String address2;
  private @Valid String postal;
  private @Valid String city;
  private @Valid String phone;
  private @Valid Integer countryId;
  private @Valid String countryName;

  /**
   **/
  public AddressDto id(Integer id) {
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
  public AddressDto label(String label) {
    this.label = label;
    return this;
  }


  @JsonProperty("label")
  public String getLabel() {
    return label;
  }

  @JsonProperty("label")
  public void setLabel(String label) {
    this.label = label;
  }

  /**
   **/
  public AddressDto name(String name) {
    this.name = name;
    return this;
  }


  @JsonProperty("name")
  public String getName() {
    return name;
  }

  @JsonProperty("name")
  public void setName(String name) {
    this.name = name;
  }

  /**
   **/
  public AddressDto address1(String address1) {
    this.address1 = address1;
    return this;
  }


  @JsonProperty("address1")
  public String getAddress1() {
    return address1;
  }

  @JsonProperty("address1")
  public void setAddress1(String address1) {
    this.address1 = address1;
  }

  /**
   **/
  public AddressDto address2(String address2) {
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
  public AddressDto postal(String postal) {
    this.postal = postal;
    return this;
  }


  @JsonProperty("postal")
  public String getPostal() {
    return postal;
  }

  @JsonProperty("postal")
  public void setPostal(String postal) {
    this.postal = postal;
  }

  /**
   **/
  public AddressDto city(String city) {
    this.city = city;
    return this;
  }


  @JsonProperty("city")
  public String getCity() {
    return city;
  }

  @JsonProperty("city")
  public void setCity(String city) {
    this.city = city;
  }

  /**
   **/
  public AddressDto phone(String phone) {
    this.phone = phone;
    return this;
  }


  @JsonProperty("phone")
  public String getPhone() {
    return phone;
  }

  @JsonProperty("phone")
  public void setPhone(String phone) {
    this.phone = phone;
  }

  /**
   **/
  public AddressDto countryId(Integer countryId) {
    this.countryId = countryId;
    return this;
  }


  @JsonProperty("countryId")
  public Integer getCountryId() {
    return countryId;
  }

  @JsonProperty("countryId")
  public void setCountryId(Integer countryId) {
    this.countryId = countryId;
  }

  /**
   **/
  public AddressDto countryName(String countryName) {
    this.countryName = countryName;
    return this;
  }


  @JsonProperty("countryName")
  public String getCountryName() {
    return countryName;
  }

  @JsonProperty("countryName")
  public void setCountryName(String countryName) {
    this.countryName = countryName;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AddressDto address = (AddressDto) o;
    return Objects.equals(this.id, address.id) && Objects.equals(this.label, address.label)
        && Objects.equals(this.name, address.name)
        && Objects.equals(this.address1, address.address1)
        && Objects.equals(this.address2, address.address2)
        && Objects.equals(this.postal, address.postal) && Objects.equals(this.city, address.city)
        && Objects.equals(this.phone, address.phone)
        && Objects.equals(this.countryId, address.countryId)
        && Objects.equals(this.countryName, address.countryName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, label, name, address1, address2, postal, city, phone, countryId,
        countryName);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AddressDto {\n");

    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    label: ").append(toIndentedString(label)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    address1: ").append(toIndentedString(address1)).append("\n");
    sb.append("    address2: ").append(toIndentedString(address2)).append("\n");
    sb.append("    postal: ").append(toIndentedString(postal)).append("\n");
    sb.append("    city: ").append(toIndentedString(city)).append("\n");
    sb.append("    phone: ").append(toIndentedString(phone)).append("\n");
    sb.append("    countryId: ").append(toIndentedString(countryId)).append("\n");
    sb.append("    countryName: ").append(toIndentedString(countryName)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }


}

