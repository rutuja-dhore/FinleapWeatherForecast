package de.finleap.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CityNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1093950714016863997L;

	private final Integer errorCode;

	private final String errorMessage;

	public CityNotFoundException(ErrorCode errorCode) {
		super(errorCode.getCode() + "-" + errorCode.getMessage());

		this.errorCode = errorCode.getCode();
		this.errorMessage = errorCode.getMessage();
	}
}