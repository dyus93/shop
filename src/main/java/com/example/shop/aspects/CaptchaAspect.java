package com.example.shop.aspects;

import lombok.extern.slf4j.Slf4j;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;

import com.example.shop.dto.ReviewDto;
import com.example.shop.exceptions.WrongCaptchaCodeException;

import javax.servlet.http.HttpSession;

@Slf4j
@Aspect
@Configuration
public class CaptchaAspect {

    @Pointcut("within(ru.geekbrains.shop.controllers..*)")
    public void controllerLayer() {}

    @Before(value = "controllerLayer() && " + "args(reviewDto, session,..))", argNames = "reviewDto, session")
    public void checkCaptcha(ReviewDto reviewDto, HttpSession session) throws WrongCaptchaCodeException {
        if (!reviewDto.getCaptchaCode().equals(session.getAttribute("captchaCode"))) {
            throw new WrongCaptchaCodeException("Error! Captcha code is incorrect!");
        }
    }

}