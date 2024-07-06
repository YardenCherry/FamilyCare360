package demo.app.logics.implementations;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN)
public class MyForbiddenException extends RuntimeException {

	private static final long serialVersionUID = 7850270618769686211L;

	public MyForbiddenException() {
		super();
	}

	public MyForbiddenException(String message) {
		super(message);
	}

	public MyForbiddenException(Throwable cause) {
		super(cause);
	}

	public MyForbiddenException(String message, Throwable cause) {
		super(message, cause);
	}
}
