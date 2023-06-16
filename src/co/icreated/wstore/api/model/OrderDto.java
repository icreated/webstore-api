package co.icreated.wstore.api.model;

import co.icreated.wstore.api.model.AddressDto;
import co.icreated.wstore.api.model.DocumentDto;
import co.icreated.wstore.api.model.DocumentLineDto;
import co.icreated.wstore.api.model.PaymentDto;
import co.icreated.wstore.api.model.ShipmentDto;
import co.icreated.wstore.api.model.ShipperDto;
import co.icreated.wstore.api.model.TaxDto;
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



@JsonTypeName("Order")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")
public class OrderDto {
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
  private @Valid ShipperDto shipper;
  private @Valid List<ShipmentDto> shipments = null;
  private @Valid List<PaymentDto> payments = null;
  private @Valid List<DocumentDto> invoices = null;
  private @Valid List<TaxDto> taxes = null;

  /**
   **/
  public OrderDto id(Integer id) {
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
  public OrderDto documentNo(String documentNo) {
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
  public OrderDto poReference(String poReference) {
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
  public OrderDto description(String description) {
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
  public OrderDto docStatus(String docStatus) {
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
  public OrderDto docStatusName(String docStatusName) {
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
  public OrderDto date(Date date) {
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
  public OrderDto totalLines(BigDecimal totalLines) {
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
  public OrderDto grandTotal(BigDecimal grandTotal) {
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
  public OrderDto name(String name) {
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
  public OrderDto lines(List<DocumentLineDto> lines) {
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

  public OrderDto addLinesItem(DocumentLineDto linesItem) {
    if (this.lines == null) {
      this.lines = new ArrayList<>();
    }

    this.lines.add(linesItem);
    return this;
  }

  public OrderDto removeLinesItem(DocumentLineDto linesItem) {
    if (linesItem != null && this.lines != null) {
      this.lines.remove(linesItem);
    }

    return this;
  }

  /**
   **/
  public OrderDto shipAddress(AddressDto shipAddress) {
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
  public OrderDto billAddress(AddressDto billAddress) {
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

  /**
   **/
  public OrderDto shipper(ShipperDto shipper) {
    this.shipper = shipper;
    return this;
  }


  @JsonProperty("shipper")
  public ShipperDto getShipper() {
    return shipper;
  }

  @JsonProperty("shipper")
  public void setShipper(ShipperDto shipper) {
    this.shipper = shipper;
  }

  /**
   **/
  public OrderDto shipments(List<ShipmentDto> shipments) {
    this.shipments = shipments;
    return this;
  }


  @JsonProperty("shipments")
  public List<ShipmentDto> getShipments() {
    return shipments;
  }

  @JsonProperty("shipments")
  public void setShipments(List<ShipmentDto> shipments) {
    this.shipments = shipments;
  }

  public OrderDto addShipmentsItem(ShipmentDto shipmentsItem) {
    if (this.shipments == null) {
      this.shipments = new ArrayList<>();
    }

    this.shipments.add(shipmentsItem);
    return this;
  }

  public OrderDto removeShipmentsItem(ShipmentDto shipmentsItem) {
    if (shipmentsItem != null && this.shipments != null) {
      this.shipments.remove(shipmentsItem);
    }

    return this;
  }

  /**
   **/
  public OrderDto payments(List<PaymentDto> payments) {
    this.payments = payments;
    return this;
  }


  @JsonProperty("payments")
  public List<PaymentDto> getPayments() {
    return payments;
  }

  @JsonProperty("payments")
  public void setPayments(List<PaymentDto> payments) {
    this.payments = payments;
  }

  public OrderDto addPaymentsItem(PaymentDto paymentsItem) {
    if (this.payments == null) {
      this.payments = new ArrayList<>();
    }

    this.payments.add(paymentsItem);
    return this;
  }

  public OrderDto removePaymentsItem(PaymentDto paymentsItem) {
    if (paymentsItem != null && this.payments != null) {
      this.payments.remove(paymentsItem);
    }

    return this;
  }

  /**
   **/
  public OrderDto invoices(List<DocumentDto> invoices) {
    this.invoices = invoices;
    return this;
  }


  @JsonProperty("invoices")
  public List<DocumentDto> getInvoices() {
    return invoices;
  }

  @JsonProperty("invoices")
  public void setInvoices(List<DocumentDto> invoices) {
    this.invoices = invoices;
  }

  public OrderDto addInvoicesItem(DocumentDto invoicesItem) {
    if (this.invoices == null) {
      this.invoices = new ArrayList<>();
    }

    this.invoices.add(invoicesItem);
    return this;
  }

  public OrderDto removeInvoicesItem(DocumentDto invoicesItem) {
    if (invoicesItem != null && this.invoices != null) {
      this.invoices.remove(invoicesItem);
    }

    return this;
  }

  /**
   **/
  public OrderDto taxes(List<TaxDto> taxes) {
    this.taxes = taxes;
    return this;
  }


  @JsonProperty("taxes")
  public List<TaxDto> getTaxes() {
    return taxes;
  }

  @JsonProperty("taxes")
  public void setTaxes(List<TaxDto> taxes) {
    this.taxes = taxes;
  }

  public OrderDto addTaxesItem(TaxDto taxesItem) {
    if (this.taxes == null) {
      this.taxes = new ArrayList<>();
    }

    this.taxes.add(taxesItem);
    return this;
  }

  public OrderDto removeTaxesItem(TaxDto taxesItem) {
    if (taxesItem != null && this.taxes != null) {
      this.taxes.remove(taxesItem);
    }

    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OrderDto order = (OrderDto) o;
    return Objects.equals(this.id, order.id) && Objects.equals(this.documentNo, order.documentNo)
        && Objects.equals(this.poReference, order.poReference)
        && Objects.equals(this.description, order.description)
        && Objects.equals(this.docStatus, order.docStatus)
        && Objects.equals(this.docStatusName, order.docStatusName)
        && Objects.equals(this.date, order.date)
        && Objects.equals(this.totalLines, order.totalLines)
        && Objects.equals(this.grandTotal, order.grandTotal)
        && Objects.equals(this.name, order.name) && Objects.equals(this.lines, order.lines)
        && Objects.equals(this.shipAddress, order.shipAddress)
        && Objects.equals(this.billAddress, order.billAddress)
        && Objects.equals(this.shipper, order.shipper)
        && Objects.equals(this.shipments, order.shipments)
        && Objects.equals(this.payments, order.payments)
        && Objects.equals(this.invoices, order.invoices) && Objects.equals(this.taxes, order.taxes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, documentNo, poReference, description, docStatus, docStatusName, date,
        totalLines, grandTotal, name, lines, shipAddress, billAddress, shipper, shipments, payments,
        invoices, taxes);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OrderDto {\n");

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
    sb.append("    shipper: ").append(toIndentedString(shipper)).append("\n");
    sb.append("    shipments: ").append(toIndentedString(shipments)).append("\n");
    sb.append("    payments: ").append(toIndentedString(payments)).append("\n");
    sb.append("    invoices: ").append(toIndentedString(invoices)).append("\n");
    sb.append("    taxes: ").append(toIndentedString(taxes)).append("\n");
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

