package qoraa.net.modules.user.controller;

import org.springframework.web.bind.annotation.RestControllerAdvice;
import qoraa.net.common.exception.BaseResponseEntityExceptionHandler;

@RestControllerAdvice(assignableTypes = WebUserController.class)
public class WebUserExceptionHandler extends BaseResponseEntityExceptionHandler {
    public WebUserExceptionHandler() {
        super();
    }
}
