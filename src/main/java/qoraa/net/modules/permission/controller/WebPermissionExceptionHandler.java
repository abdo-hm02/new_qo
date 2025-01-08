package qoraa.net.modules.permission.controller;

import org.springframework.web.bind.annotation.RestControllerAdvice;
import qoraa.net.common.exception.BaseResponseEntityExceptionHandler;

@RestControllerAdvice(assignableTypes = WebPermissionController.class)
public class WebPermissionExceptionHandler extends BaseResponseEntityExceptionHandler {
    public WebPermissionExceptionHandler() {
        super();
    }
}

