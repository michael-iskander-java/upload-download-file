package sa.com.stc.file.exception;

public class AuthenticationException extends GenericException {

  private static final long serialVersionUID = -8441001442185130306L;

  public AuthenticationException(String message) {
    super(message);
  }

  public AuthenticationException(String message, Throwable cause) {
    super(message, cause);
  }

  public AuthenticationException(ErrorReason errorReason, Object... reasonParams) {
    super(errorReason, reasonParams);
  }

  public AuthenticationException(ErrorReason errorReason, Throwable cause) {
    super(errorReason, cause);
  }

  public AuthenticationException(ErrorReason errorReason, Object reasonParam, Throwable cause) {
    super(errorReason, reasonParam, cause);
  }

  public AuthenticationException(ErrorReason errorReason, Object reasonParam1, Object reasonParam2,
      Throwable cause) {
    super(errorReason, reasonParam1, reasonParam2, cause);
  }
}
