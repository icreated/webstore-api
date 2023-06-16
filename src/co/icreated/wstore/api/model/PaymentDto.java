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



@JsonTypeName("Payment")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")
public class PaymentDto {
  private @Valid Integer id;
  private @Valid String documentNo;
  private @Valid String description;
  private @Valid String docStatus;
  private @Valid BigDecimal payAmt;
  private @Valid String trxid;
  private @Valid String currency;
  private @Valid String tenderType;

  /**
   **/
  public PaymentDto id(Integer id) {
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
  public PaymentDto documentNo(String documentNo) {
    this.documentNo = documentNo;
    return this;
  }


  @JsonProperty("documentNo")
  public String getDocumentNo() {
    return documentNo;
  }

  @JsonProperty("documentNo")
  public void setDocumentNo(String documentNo) {
    this.documentNo = documentNo;
  }

  /**
   **/
  public PaymentDto description(String description) {
    this.description = description;
    return this;
  }


  @JsonProperty("description")
  public String getDescription() {
    return description;
  }

  @JsonProperty("description")
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   **/
  public PaymentDto docStatus(String docStatus) {
    this.docStatus = docStatus;
    return this;
  }


  @JsonProperty("docStatus")
  public String getDocStatus() {
    return docStatus;
  }

  @JsonProperty("docStatus")
  public void setDocStatus(String docStatus) {
    this.docStatus = docStatus;
  }

  /**
   **/
  public PaymentDto payAmt(BigDecimal payAmt) {
    this.payAmt = payAmt;
    return this;
  }


  @JsonProperty("payAmt")
  public BigDecimal getPayAmt() {
    return payAmt;
  }

  @JsonProperty("payAmt")
  public void setPayAmt(BigDecimal payAmt) {
    this.payAmt = payAmt;
  }

  /**
   **/
  public PaymentDto trxid(String trxid) {
    this.trxid = trxid;
    return this;
  }


  @JsonProperty("trxid")
  public String getTrxid() {
    return trxid;
  }

  @JsonProperty("trxid")
  public void setTrxid(String trxid) {
    this.trxid = trxid;
  }

  /**
   **/
  public PaymentDto currency(String currency) {
    this.currency = currency;
    return this;
  }


  @JsonProperty("currency")
  public String getCurrency() {
    return currency;
  }

  @JsonProperty("currency")
  public void setCurrency(String currency) {
    this.currency = currency;
  }

  /**
   **/
  public PaymentDto tenderType(String tenderType) {
    this.tenderType = tenderType;
    return this;
  }


  @JsonProperty("tenderType")
  public String getTenderType() {
    return tenderType;
  }

  @JsonProperty("tenderType")
  public void setTenderType(String tenderType) {
    this.tenderType = tenderType;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PaymentDto payment = (PaymentDto) o;
    return Objects.equals(this.id, payment.id)
        && Objects.equals(this.documentNo, payment.documentNo)
        && Objects.equals(this.description, payment.description)
        && Objects.equals(this.docStatus, payment.docStatus)
        && Objects.equals(this.payAmt, payment.payAmt) && Objects.equals(this.trxid, payment.trxid)
        && Objects.equals(this.currency, payment.currency)
        && Objects.equals(this.tenderType, payment.tenderType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, documentNo, description, docStatus, payAmt, trxid, currency,
        tenderType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PaymentDto {\n");

    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    documentNo: ").append(toIndentedString(documentNo)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    docStatus: ").append(toIndentedString(docStatus)).append("\n");
    sb.append("    payAmt: ").append(toIndentedString(payAmt)).append("\n");
    sb.append("    trxid: ").append(toIndentedString(trxid)).append("\n");
    sb.append("    currency: ").append(toIndentedString(currency)).append("\n");
    sb.append("    tenderType: ").append(toIndentedString(tenderType)).append("\n");
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

