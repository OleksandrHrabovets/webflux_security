package ua.oh.webflux_security.exception;

public class AuthException extends ApiException {

  public AuthException(String message, String errorCode) {
    super(message, errorCode);
  }
}
