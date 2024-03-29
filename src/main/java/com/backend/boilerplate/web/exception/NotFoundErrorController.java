package com.backend.boilerplate.web.exception;

import com.sp.boilerplate.commons.constant.Status;
import com.sp.boilerplate.commons.dto.Response;
import com.sp.boilerplate.commons.util.ErrorGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

/**
 * @author sarvesh
 * @version 0.0.1
 * @since 0.0.1
 */
@RestController
@RequestMapping("${error.path:/error}")
public class NotFoundErrorController implements ErrorController {

    @Value("${error.path:/error}")
    private String errorPath;

    @GetMapping
    public ResponseEntity<Response> error(WebRequest request) {
        Response errorResponse = new Response<Void>(Status.FAIL, HttpStatus.NOT_FOUND.value(),
            ErrorGenerator.generateForCode("1003"));
        return new ResponseEntity<>(errorResponse, new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

}
