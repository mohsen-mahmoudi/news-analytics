package com.demo.microservice_2021.elastic.query.web.client.common.api.error.handler;

import com.demo.microservice_2021.elastic.query.web.client.common.model.ElasticQueryWebClientRequestModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ElasticQueryWebClientServiceErrorHandler {

    private static final Logger LOG = LoggerFactory.getLogger(ElasticQueryWebClientServiceErrorHandler.class);

    @ExceptionHandler(AccessDeniedException.class)
    public String handler(AccessDeniedException e, Model model) {
        LOG.error("Access denied exception: {}", e.getMessage());
        model.addAttribute("error", HttpStatus.FORBIDDEN.getReasonPhrase());
        model.addAttribute("error_description", "You are not authorized to access this resource");
        return "error";
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public String handler(IllegalArgumentException e, Model model) {
        LOG.info("Illegal argument exception!", e);
        model.addAttribute("error", HttpStatus.BAD_REQUEST.getReasonPhrase());
        model.addAttribute("error_description", "Illegal argument exception! " + e.getMessage());
        return "error";
    }

    @ExceptionHandler(Exception.class)
    public String handler(Exception e, Model model) {
        LOG.error("Service exception!", e);
        model.addAttribute("error", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        model.addAttribute("error_description", "Service exception! " + e.getMessage());
        return "error";
    }

    @ExceptionHandler(RuntimeException.class)
    public String handler(RuntimeException e,  Model model) {
        LOG.error("Service runtime exception!", e);
        model.addAttribute("elasticQueryWebClientRequestModel", ElasticQueryWebClientRequestModel.builder().build());
        model.addAttribute("error", HttpStatus.BAD_REQUEST.getReasonPhrase());
        model.addAttribute("error_description", "Service runtime exception! " + e.getMessage());
        return "home";
    }

    @ExceptionHandler(BindException.class)
    public String handler(BindException e,  Model model) {
        LOG.error("Method argument not valid exception!", e);
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach(error -> {
            errors.put(((FieldError) error).getField(), error.getDefaultMessage());
        });
        model.addAttribute("elasticQueryWebClientRequestModel", ElasticQueryWebClientRequestModel.builder().build());
        model.addAttribute("error", HttpStatus.BAD_REQUEST.getReasonPhrase());
        model.addAttribute("error_description", errors);
        return "home";
    }
}
