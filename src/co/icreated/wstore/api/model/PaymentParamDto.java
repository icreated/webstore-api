package co.icreated.wstore.api.model;

import com.fasterxml.jackson.annotation.JsonTypeName;
import javax.validation.constraints.*;
import javax.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;



@JsonTypeName("PaymentParam")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")
public class PaymentParamDto {
  private @Valid String type;
  private @Valid Integer orderId;

  /**
   **/
  public PaymentParamDto type(String type) {
    this.type = type;
    return this;
  }


  @JsonProperty("type")
  public String getType() {
    return type;
  }

  @JsonProperty("type")
  public void setType(String type) {
    this.type = type;
  }

  /**
   **/
  public PaymentParamDto orderId(Integer orderId) {
    this.orderId = orderId;
    return this;
  }


  @JsonProperty("orderId")
  public Integer getOrderId() {
    return orderId;
  }

  @JsonProperty("orderId")
  public void setOrderId(Integer orderId) {
    this.orderId = orderId;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PaymentParamDto paymentParam = (PaymentParamDto) o;
    return Objects.equals(this.type, paymentParam.type)
        && Objects.equals(this.orderId, paymentParam.orderId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, orderId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PaymentParamDto {\n");

    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    orderId: ").append(toIndentedString(orderId)).append("\n");
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

