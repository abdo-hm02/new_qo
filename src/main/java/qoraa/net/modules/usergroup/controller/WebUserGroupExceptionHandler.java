package qoraa.net.modules.usergroup.controller;

import org.springframework.web.bind.annotation.RestControllerAdvice;
import qoraa.net.common.exception.BaseResponseEntityExceptionHandler;

@RestControllerAdvice(assignableTypes = WebUserGroupController.class)
public class WebUserGroupExceptionHandler extends BaseResponseEntityExceptionHandler {
    public WebUserGroupExceptionHandler() {
	super();
    }
}
