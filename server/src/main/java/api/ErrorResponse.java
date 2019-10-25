package api;

/**
 * API error response.
 */
public class ErrorResponse {
  public String errorCode;
  public String errorMessage;

  public String getErrorCode() {
    return errorCode;
  }

  public ErrorResponse setErrorCode(String errorCode) {
    this.errorCode = errorCode;
    return this;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public ErrorResponse setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
    return this;
  }
}
