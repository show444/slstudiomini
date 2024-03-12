package com.example.slstudiomini.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import jakarta.persistence.NoResultException;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    // すべての例外タイプを捕捉
    @ExceptionHandler(Exception.class)
    public Object handleAllExceptions(Exception ex) {
        ModelAndView mav = new ModelAndView("error/custom-error");
        mav.addObject("errorMessage", "開発者の恥です。ごめんなさい。");
        return mav;
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public Object handleDataIntegrityViolationException(Exception ex) {
        ModelAndView mav = new ModelAndView("error/custom-error");
        mav.addObject("errorMessage", "ユニーク性違反でエラーが出た可能性があります");
        return mav;
    }

    @ExceptionHandler(NoResultException.class)
    public Object handleNoResultException(Exception ex) {
        ModelAndView mav = new ModelAndView("error/custom-error");
        mav.addObject("errorMessage", "存在しないコースです");
        return mav;
    }

    @ExceptionHandler(MyLessonNotFoundException.class)
    public Object handleMyLessonNotFoundException(Exception ex) {
        logger.error("不正なLessonへのアクセスが行われました。エラー詳細:" + ex.getMessage());

        ModelAndView mav = new ModelAndView("error/custom-error");
        mav.addObject("errorMessage", ex.getMessage());
        return mav;
    }
}
