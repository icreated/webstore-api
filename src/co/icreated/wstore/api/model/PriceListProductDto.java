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



@JsonTypeName("PriceListProduct")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")
public class PriceListProductDto {
  private @Valid Integer id;
  private @Valid String value;
  private @Valid String name;
  private @Valid String description;
  private @Valid String help;
  private @Valid String documentNote;
  private @Valid String imageURL;
  private @Valid BigDecimal price;
  private @Valid Integer qty;
  private @Valid Integer line;

  /**
   **/
  public PriceListProductDto id(Integer id) {
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
  public PriceListProductDto value(String value) {
    this.value = value;
    return this;
  }


  @JsonProperty("value")
  public String getValue() {
    return value;
  }

  @JsonProperty("value")
  public void setValue(String value) {
    this.value = value;
  }

  /**
   **/
  public PriceListProductDto name(String name) {
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
  public PriceListProductDto description(String description) {
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
  public PriceListProductDto help(String help) {
    this.help = help;
    return this;
  }


  @JsonProperty("help")
  public String getHelp() {
    return help;
  }

  @JsonProperty("help")
  public void setHelp(String help) {
    this.help = help;
  }

  /**
   **/
  public PriceListProductDto documentNote(String documentNote) {
    this.documentNote = documentNote;
    return this;
  }


  @JsonProperty("documentNote")
  public String getDocumentNote() {
    return documentNote;
  }

  @JsonProperty("documentNote")
  public void setDocumentNote(String documentNote) {
    this.documentNote = documentNote;
  }

  /**
   **/
  public PriceListProductDto imageURL(String imageURL) {
    this.imageURL = imageURL;
    return this;
  }


  @JsonProperty("imageURL")
  public String getImageURL() {
    return imageURL;
  }

  @JsonProperty("imageURL")
  public void setImageURL(String imageURL) {
    this.imageURL = imageURL;
  }

  /**
   **/
  public PriceListProductDto price(BigDecimal price) {
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
  public PriceListProductDto qty(Integer qty) {
    this.qty = qty;
    return this;
  }


  @JsonProperty("qty")
  public Integer getQty() {
    return qty;
  }

  @JsonProperty("qty")
  public void setQty(Integer qty) {
    this.qty = qty;
  }

  /**
   **/
  public PriceListProductDto line(Integer line) {
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


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PriceListProductDto priceListProduct = (PriceListProductDto) o;
    return Objects.equals(this.id, priceListProduct.id)
        && Objects.equals(this.value, priceListProduct.value)
        && Objects.equals(this.name, priceListProduct.name)
        && Objects.equals(this.description, priceListProduct.description)
        && Objects.equals(this.help, priceListProduct.help)
        && Objects.equals(this.documentNote, priceListProduct.documentNote)
        && Objects.equals(this.imageURL, priceListProduct.imageURL)
        && Objects.equals(this.price, priceListProduct.price)
        && Objects.equals(this.qty, priceListProduct.qty)
        && Objects.equals(this.line, priceListProduct.line);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, value, name, description, help, documentNote, imageURL, price, qty,
        line);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PriceListProductDto {\n");

    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    value: ").append(toIndentedString(value)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    help: ").append(toIndentedString(help)).append("\n");
    sb.append("    documentNote: ").append(toIndentedString(documentNote)).append("\n");
    sb.append("    imageURL: ").append(toIndentedString(imageURL)).append("\n");
    sb.append("    price: ").append(toIndentedString(price)).append("\n");
    sb.append("    qty: ").append(toIndentedString(qty)).append("\n");
    sb.append("    line: ").append(toIndentedString(line)).append("\n");
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

