package demo.app.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class MyNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 6195487277902365417L;

	public MyNotFoundException() {
		super();
	}

	public MyNotFoundException(String message) {
		super(message);
	}

	public MyNotFoundException(Throwable cause) {
		super(cause);
	}

	public MyNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
