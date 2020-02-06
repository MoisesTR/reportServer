package com.laboratorio.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.exolab.castor.types.DateTime;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
public class ApiException {

    private final String message;
    private final HttpStatus httpStatus;
    private final DateTime date;

}
