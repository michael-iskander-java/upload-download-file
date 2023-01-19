package sa.com.stc.file.exception;

public enum ErrorReason {


  USER_NOT_AUTHORIZED("You are not authorized person to perform this action"),
  INVALID_EMAIL("Invalid email for user"),
  INVALID_GROUP_NAME("Invalid group name"),
  ITEM_NOT_FOUND("Item not found"),
  INVALID_ITEM("Invalid item"),
  FILE_UPLOADING_ERROR("Uploading file error"),
  INVALID_FILE_ID("File is not found");
  
  private String description;

  ErrorReason(String description) {
    this.description = description;
  }

  public String getDescription() {
    return description;
  }

}
