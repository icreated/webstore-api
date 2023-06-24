package co.icreated.wstore.api.model;

import com.fasterxml.jackson.annotation.JsonTypeName;
import java.math.BigDecimal;
import javax.validation.constraints.*;
import javax.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;



@JsonTypeName("Tax")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")
public class TaxDto   {
  private @Valid String name;
  private @Valid BigDecimal taxAmt;

  /**
   **/
  public TaxDto name(String name) {
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
  public TaxDto taxAmt(BigDecimal taxAmt) {
    this.taxAmt = taxAmt;
    return this;
  }

  
  @JsonProperty("taxAmt")
  public BigDecimal getTaxAmt() {
    return taxAmt;
  }

  @JsonProperty("taxAmt")
  public void setTaxAmt(BigDecimal taxAmt) {
    this.taxAmt = taxAmt;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TaxDto tax = (TaxDto) o;
    return Objects.equals(this.name, tax.name) &&
        Objects.equals(this.taxAmt, tax.taxAmt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, taxAmt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TaxDto {\n");
    
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    taxAmt: ").append(toIndentedString(taxAmt)).append("\n");
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

