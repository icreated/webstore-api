package co.icreated.wstore.api.model;

import co.icreated.wstore.api.model.DocumentLineDto;
import com.fasterxml.jackson.annotation.JsonTypeName;
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



@JsonTypeName("Shipment")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")
public class ShipmentDto   {
  private @Valid Integer id;
  private @Valid String documentNo;
  private @Valid String description;
  private @Valid String docStatus;
  private @Valid String docStatusName;
  private @Valid Date date;
  private @Valid List<DocumentLineDto> lines = null;
  private @Valid String trackingNo;

  /**
   **/
  public ShipmentDto id(Integer id) {
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
  public ShipmentDto documentNo(String documentNo) {
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
  public ShipmentDto description(String description) {
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
  public ShipmentDto docStatus(String docStatus) {
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
  public ShipmentDto docStatusName(String docStatusName) {
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
  public ShipmentDto date(Date date) {
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
  public ShipmentDto lines(List<DocumentLineDto> lines) {
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

  public ShipmentDto addLinesItem(DocumentLineDto linesItem) {
    if (this.lines == null) {
      this.lines = new ArrayList<>();
    }

    this.lines.add(linesItem);
    return this;
  }

  public ShipmentDto removeLinesItem(DocumentLineDto linesItem) {
    if (linesItem != null && this.lines != null) {
      this.lines.remove(linesItem);
    }

    return this;
  }
  /**
   **/
  public ShipmentDto trackingNo(String trackingNo) {
    this.trackingNo = trackingNo;
    return this;
  }

  
  @JsonProperty("trackingNo")
  public String getTrackingNo() {
    return trackingNo;
  }

  @JsonProperty("trackingNo")
  public void setTrackingNo(String trackingNo) {
    this.trackingNo = trackingNo;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ShipmentDto shipment = (ShipmentDto) o;
    return Objects.equals(this.id, shipment.id) &&
        Objects.equals(this.documentNo, shipment.documentNo) &&
        Objects.equals(this.description, shipment.description) &&
        Objects.equals(this.docStatus, shipment.docStatus) &&
        Objects.equals(this.docStatusName, shipment.docStatusName) &&
        Objects.equals(this.date, shipment.date) &&
        Objects.equals(this.lines, shipment.lines) &&
        Objects.equals(this.trackingNo, shipment.trackingNo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, documentNo, description, docStatus, docStatusName, date, lines, trackingNo);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ShipmentDto {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    documentNo: ").append(toIndentedString(documentNo)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    docStatus: ").append(toIndentedString(docStatus)).append("\n");
    sb.append("    docStatusName: ").append(toIndentedString(docStatusName)).append("\n");
    sb.append("    date: ").append(toIndentedString(date)).append("\n");
    sb.append("    lines: ").append(toIndentedString(lines)).append("\n");
    sb.append("    trackingNo: ").append(toIndentedString(trackingNo)).append("\n");
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

