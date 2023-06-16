package co.icreated.wstore.api.model;

import co.icreated.wstore.api.model.AddressDto;
import co.icreated.wstore.api.model.DocumentLineDto;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.validation.constraints.*;
import javax.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;



@JsonTypeName("Document")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")
public class DocumentDto {
  private @Valid Integer id;
  private @Valid String documentNo;
  private @Valid String poReference;
  private @Valid String description;
  private @Valid String docStatus;
  private @Valid String docStatusName;
  private @Valid Date date;
  private @Valid BigDecimal totalLines;
  private @Valid BigDecimal grandTotal;
  private @Valid String name;
  private @Valid List<DocumentLineDto> lines = null;
  private @Valid AddressDto shipAddress;
  private @Valid AddressDto billAddress;

  /**
   **/
  public DocumentDto id(Integer id) {
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
  public DocumentDto documentNo(String documentNo) {
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
  public DocumentDto poReference(String poReference) {
    this.poReference = poReference;
    return this;
  }


  @JsonProperty("poReference")
  public String getPoReference() {
    return poReference;
  }

  @JsonProperty("poReference")
  public void setPoReference(String poReference) {
    this.poReference = poReference;
  }

  /**
   **/
  public DocumentDto description(String description) {
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
  public DocumentDto docStatus(String docStatus) {
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
  public DocumentDto docStatusName(String docStatusName) {
    this.docStatusName = docStatusName;
    return this;
  }


  @JsonProperty("docStatusName")
  public String getDocStatusName() {
    return docStatusName;
  }

  @JsonProperty("docStatusName")
  public void setDocStatusName(String docStatusName) {
    this.docStatusName = docStatusName;
  }

  /**
   **/
  public DocumentDto date(Date date) {
    this.date = date;
    return this;
  }


  @JsonProperty("date")
  public Date getDate() {
    return date;
  }

  @JsonProperty("date")
  public void setDate(Date date) {
    this.date = date;
  }

  /**
   **/
  public DocumentDto totalLines(BigDecimal totalLines) {
    this.totalLines = totalLines;
    return this;
  }


  @JsonProperty("totalLines")
  public BigDecimal getTotalLines() {
    return totalLines;
  }

  @JsonProperty("totalLines")
  public void setTotalLines(BigDecimal totalLines) {
    this.totalLines = totalLines;
  }

  /**
   **/
  public DocumentDto grandTotal(BigDecimal grandTotal) {
    this.grandTotal = grandTotal;
    return this;
  }


  @JsonProperty("grandTotal")
  public BigDecimal getGrandTotal() {
    return grandTotal;
  }

  @JsonProperty("grandTotal")
  public void setGrandTotal(BigDecimal grandTotal) {
    this.grandTotal = grandTotal;
  }

  /**
   **/
  public DocumentDto name(String name) {
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
  public DocumentDto lines(List<DocumentLineDto> lines) {
    this.lines = lines;
    return this;
  }


  @JsonProperty("lines")
  public List<DocumentLineDto> getLines() {
    return lines;
  }

  @JsonProperty("lines")
  public void setLines(List<DocumentLineDto> lines) {
    this.lines = lines;
  }

  public DocumentDto addLinesItem(DocumentLineDto linesItem) {
    if (this.lines == null) {
      this.lines = new ArrayList<>();
    }

    this.lines.add(linesItem);
    return this;
  }

  public DocumentDto removeLinesItem(DocumentLineDto linesItem) {
    if (linesItem != null && this.lines != null) {
      this.lines.remove(linesItem);
    }

    return this;
  }

  /**
   **/
  public DocumentDto shipAddress(AddressDto shipAddress) {
    this.shipAddress = shipAddress;
    return this;
  }


  @JsonProperty("shipAddress")
  public AddressDto getShipAddress() {
    return shipAddress;
  }

  @JsonProperty("shipAddress")
  public void setShipAddress(AddressDto shipAddress) {
    this.shipAddress = shipAddress;
  }

  /**
   **/
  public DocumentDto billAddress(AddressDto billAddress) {
    this.billAddress = billAddress;
    return this;
  }


  @JsonProperty("billAddress")
  public AddressDto getBillAddress() {
    return billAddress;
  }

  @JsonProperty("billAddress")
  public void setBillAddress(AddressDto billAddress) {
    this.billAddress = billAddress;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DocumentDto document = (DocumentDto) o;
    return Objects.equals(this.id, document.id)
        && Objects.equals(this.documentNo, document.documentNo)
        && Objects.equals(this.poReference, document.poReference)
        && Objects.equals(this.description, document.description)
        && Objects.equals(this.docStatus, document.docStatus)
        && Objects.equals(this.docStatusName, document.docStatusName)
        && Objects.equals(this.date, document.date)
        && Objects.equals(this.totalLines, document.totalLines)
        && Objects.equals(this.grandTotal, document.grandTotal)
        && Objects.equals(this.name, document.name) && Objects.equals(this.lines, document.lines)
        && Objects.equals(this.shipAddress, document.shipAddress)
        && Objects.equals(this.billAddress, document.billAddress);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, documentNo, poReference, description, docStatus, docStatusName, date,
        totalLines, grandTotal, name, lines, shipAddress, billAddress);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DocumentDto {\n");

    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    documentNo: ").append(toIndentedString(documentNo)).append("\n");
    sb.append("    poReference: ").append(toIndentedString(poReference)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    docStatus: ").append(toIndentedString(docStatus)).append("\n");
    sb.append("    docStatusName: ").append(toIndentedString(docStatusName)).append("\n");
    sb.append("    date: ").append(toIndentedString(date)).append("\n");
    sb.append("    totalLines: ").append(toIndentedString(totalLines)).append("\n");
    sb.append("    grandTotal: ").append(toIndentedString(grandTotal)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    lines: ").append(toIndentedString(lines)).append("\n");
    sb.append("    shipAddress: ").append(toIndentedString(shipAddress)).append("\n");
    sb.append("    billAddress: ").append(toIndentedString(billAddress)).append("\n");
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

