package com.tanda.payment_api.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpServerErrorException;

@Getter
@Setter
public class HttpStatusException extends HttpServerErrorException {

    private HttpStatus status;

    private String message;

    public HttpStatusException(HttpStatus status, String message) {
        super(status, message);
        this.status = status;
        this.message = message;
    }
    public static HttpStatusException failed(String message) {
        return new HttpStatusException(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }

    public static HttpStatusException notFound(String message) {
        return new HttpStatusException(HttpStatus.NOT_FOUND, message);
    }

    public static HttpStatusException duplicate(String message) {
        return new HttpStatusException(HttpStatus.CONFLICT, message);
    }

    public static HttpStatusException forbidden(String message) {
        return new HttpStatusException(HttpStatus.FORBIDDEN, message);
    }

    public static HttpStatusException unAuthorized(String message) {
        return new HttpStatusException(HttpStatus.UNAUTHORIZED, message);
    }

    public static HttpStatusException badRequest(String message) {
        return new HttpStatusException(HttpStatus.BAD_REQUEST, message);
    }

    public static HttpStatusException unAcceptable(String message) {
        return new HttpStatusException(HttpStatus.NOT_ACCEPTABLE, message);
    }

    public static HttpStatusException gone(String message) {
        return new HttpStatusException(HttpStatus.GONE, message);
    }

}
