package de.finleap.exception;

import lombok.Getter;

public enum ErrorCode {

	VALIDATION_INVALID_ZONE_ID(1001, "Invalid Zone Id"),

	SOMETHING_WENT_WRONG(1002, "Something went wrong"),

	UNEXPECTED_ERROR(9999, "Internal Server Error"),

	NOTHING_TO_GEOCODE(400, "Invalid API key"),

	VALIDATION_INVALID_API_KEY(401, "Invalid API key"),

	CITY_NOT_FOUND(404, "City Not Found");

	@Getter
	private Integer code;

	@Getter
	private String message;

	ErrorCode(Integer code, String message) {
		this.code = code;
		this.message = message;
	}

}