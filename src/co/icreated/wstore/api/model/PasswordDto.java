package co.icreated.wstore.api.model;

import com.fasterxml.jackson.annotation.JsonTypeName;
import javax.validation.constraints.*;
import javax.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;



@JsonTypeName("Password")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")
public class PasswordDto {
  private @Valid String password;
  private @Valid String newPassword;
  private @Valid String confirmPassword;

  /**
   **/
  public PasswordDto password(String password) {
    this.password = password;
    return this;
  }


  @JsonProperty("password")
  public String getPassword() {
    return password;
  }

  @JsonProperty("password")
  public void setPassword(String password) {
    this.password = password;
  }

  /**
   **/
  public PasswordDto newPassword(String newPassword) {
    this.newPassword = newPassword;
    return this;
  }


  @JsonProperty("newPassword")
  public String getNewPassword() {
    return newPassword;
  }

  @JsonProperty("newPassword")
  public void setNewPassword(String newPassword) {
    this.newPassword = newPassword;
  }

  /**
   **/
  public PasswordDto confirmPassword(String confirmPassword) {
    this.confirmPassword = confirmPassword;
    return this;
  }


  @JsonProperty("confirmPassword")
  public String getConfirmPassword() {
    return confirmPassword;
  }

  @JsonProperty("confirmPassword")
  public void setConfirmPassword(String confirmPassword) {
    this.confirmPassword = confirmPassword;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PasswordDto password = (PasswordDto) o;
    return Objects.equals(this.password, password.password)
        && Objects.equals(this.newPassword, password.newPassword)
        && Objects.equals(this.confirmPassword, password.confirmPassword);
  }

  @Override
  public int hashCode() {
    return Objects.hash(password, newPassword, confirmPassword);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PasswordDto {\n");

    sb.append("    password: ").append(toIndentedString(password)).append("\n");
    sb.append("    newPassword: ").append(toIndentedString(newPassword)).append("\n");
    sb.append("    confirmPassword: ").append(toIndentedString(confirmPassword)).append("\n");
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

