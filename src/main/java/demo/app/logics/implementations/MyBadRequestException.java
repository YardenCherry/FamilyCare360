package demo.app.logics.implementations;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class MyBadRequestException extends RuntimeException {

	private static final long serialVersionUID = 3660868794559161299L;

	public MyBadRequestException() {
		super();
	}

	public MyBadRequestException(String message) {
		super(message);
	}

	public MyBadRequestException(Throwable cause) {
		super(cause);
	}

	public MyBadRequestException(String message, Throwable cause) {
		super(message, cause);
	}
}
