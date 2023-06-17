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



@JsonTypeName("DocumentLine")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")
public class DocumentLineDto   {
  private @Valid Integer id;
  private @Valid Integer productId;
  private @Valid Integer line;
  private @Valid String name;
  private @Valid String description;
  private @Valid BigDecimal priceList;
  private @Valid BigDecimal price;
  private @Valid BigDecimal qty;
  private @Valid BigDecimal lineNetAmt;

  /**
   **/
  public DocumentLineDto id(Integer id) {
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
  public DocumentLineDto productId(Integer productId) {
    this.productId = productId;
    return this;
  }

  
  @JsonProperty("productId")
  public Integer getProductId() {
    return productId;
  }

  @JsonProperty("productId")
  public void setProductId(Integer productId) {
    this.productId = productId;
  }

  /**
   **/
  public DocumentLineDto line(Integer line) {
    this.line = line;
    return this;
  }

  
  @JsonProperty("line")
  public Integer getLine() {
    return line;
  }

  @JsonProperty("line")
  public void setLine(Integer line) {
    this.line = line;
  }

  /**
   **/
  public DocumentLineDto name(String name) {
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
  public DocumentLineDto description(String description) {
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
  public DocumentLineDto priceList(BigDecimal priceList) {
    this.priceList = priceList;
    return this;
  }

  
  @JsonProperty("priceList")
  public BigDecimal getPriceList() {
    return priceList;
  }

  @JsonProperty("priceList")
  public void setPriceList(BigDecimal priceList) {
    this.priceList = priceList;
  }

  /**
   **/
  public DocumentLineDto price(BigDecimal price) {
    this.price = price;
    return this;
  }

  
  @JsonProperty("price")
  public BigDecimal getPrice() {
    return price;
  }

  @JsonProperty("price")
  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  /**
   **/
  public DocumentLineDto qty(BigDecimal qty) {
    this.qty = qty;
    return this;
  }

  
  @JsonProperty("qty")
  public BigDecimal getQty() {
    return qty;
  }

  @JsonProperty("qty")
  public void setQty(BigDecimal qty) {
    this.qty = qty;
  }

  /**
   **/
  public DocumentLineDto lineNetAmt(BigDecimal lineNetAmt) {
    this.lineNetAmt = lineNetAmt;
    return this;
  }

  
  @JsonProperty("lineNetAmt")
  public BigDecimal getLineNetAmt() {
    return lineNetAmt;
  }

  @JsonProperty("lineNetAmt")
  public void setLineNetAmt(BigDecimal lineNetAmt) {
    this.lineNetAmt = lineNetAmt;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DocumentLineDto documentLine = (DocumentLineDto) o;
    return Objects.equals(this.id, documentLine.id) &&
        Objects.equals(this.productId, documentLine.productId) &&
        Objects.equals(this.line, documentLine.line) &&
        Objects.equals(this.name, documentLine.name) &&
        Objects.equals(this.description, documentLine.description) &&
        Objects.equals(this.priceList, documentLine.priceList) &&
        Objects.equals(this.price, documentLine.price) &&
        Objects.equals(this.qty, documentLine.qty) &&
        Objects.equals(this.lineNetAmt, documentLine.lineNetAmt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, productId, line, name, description, priceList, price, qty, lineNetAmt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DocumentLineDto {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    productId: ").append(toIndentedString(productId)).append("\n");
    sb.append("    line: ").append(toIndentedString(line)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    priceList: ").append(toIndentedString(priceList)).append("\n");
    sb.append("    price: ").append(toIndentedString(price)).append("\n");
    sb.append("    qty: ").append(toIndentedString(qty)).append("\n");
    sb.append("    lineNetAmt: ").append(toIndentedString(lineNetAmt)).append("\n");
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

