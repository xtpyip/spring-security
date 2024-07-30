package com.pyip.exception;
import org.springframework.security.core.AuthenticationException;
/**
 * @ClassName: ValidateCodeException
 * @version: 1.0
 * @Author: pyipXt
 * @Description: 验证码相关异常
 **/

public class ValidateCodeException extends AuthenticationException {

    public ValidateCodeException(String msg) {
        super(msg);
    }
}
