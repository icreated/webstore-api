package co.icreated.wstore.exception;

public class WstoreError {
  String name;
  String message;
  String stack;


  public WstoreError(String name, String message) {
    super();
    this.name = name;
    this.message = message;
  }

  public WstoreError(String name, String message, String stack) {
    super();
    this.name = name;
    this.message = message;
    this.stack = stack;
  }

  public String getName() {
    return name;
  }

  public String getMessage() {
    return message;
  }

  public String getStack() {
    return stack;
  }
}
