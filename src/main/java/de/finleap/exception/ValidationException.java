package de.finleap.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValidationException extends RuntimeException {

	private static final long serialVersionUID = -1531884894591066721L;

	private final Integer errorCode;

	private final String errorMessage;

	public ValidationException(ErrorCode errorCode) {
		super(errorCode.getCode() + "-" + errorCode.getMessage());

		this.errorCode = errorCode.getCode();
		this.errorMessage = errorCode.getMessage();
	}
}