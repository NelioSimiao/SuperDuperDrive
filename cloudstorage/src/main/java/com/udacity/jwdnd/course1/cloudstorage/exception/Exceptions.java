package com.udacity.jwdnd.course1.cloudstorage.exception;

import com.udacity.jwdnd.course1.cloudstorage.constants.Constants;
import org.apache.tomcat.util.bcel.classfile.Constant;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.SQLDataException;

@ControllerAdvice
public class Exceptions {
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String handleMaxSizeException(MaxUploadSizeExceededException exc, RedirectAttributes redirectAttributes){
        redirectAttributes.addAttribute("error", true);
        redirectAttributes.addAttribute("message", Constants.FIlE_EXCEED_SIZE);
        return "redirect:/home";
    }


    @ExceptionHandler(SQLDataException.class)
    public String SQLException(SQLDataException exc, RedirectAttributes redirectAttributes){
        redirectAttributes.addAttribute("error", true);
        redirectAttributes.addAttribute("message", "Value too long for column NOTEDESCRIPTION VARCHAR(1000)");
        return "redirect:/home";
    }
}
