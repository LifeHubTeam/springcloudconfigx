package cn.lifehub.config.controller;


import cn.lifehub.config.exception.AuthCheckException;
import cn.lifehub.config.models.BaseResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(value = AuthCheckException.class)
    @ResponseBody
    public ResponseEntity<Object> authCheckException(HttpServletRequest request, AuthCheckException ac) {
        return new ResponseEntity<Object>(BaseResult.notAllow(), HttpStatus.OK);
    }

}
