package ru.neoflex.learning.creaditpipeline.deal.controller;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.neoflex.learning.creaditpipeline.deal.model.ErrorMessage;

import java.time.OffsetDateTime;

@Slf4j
@RestControllerAdvice
public class DealExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorMessage handleNotFoundException(HttpServletRequest request, EntityNotFoundException e) {
        log.error("handleNotFoundException() - exception = {}", ExceptionUtils.getStackTrace(e));

        return getErrorMessage(request, HttpStatus.NOT_FOUND, e);
    }

    private ErrorMessage getErrorMessage(HttpServletRequest request, HttpStatus status, Exception e) {
        return ErrorMessage.builder()
            .timestamp(OffsetDateTime.now())
            .exception(e.getClass().getSimpleName())
            .status(status.value())
            .error(status.getReasonPhrase())
            .path(request.getRequestURI())
            .message(e.getLocalizedMessage())
            .build();
    }

}
