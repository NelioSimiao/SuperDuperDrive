package com.udacity.jwdnd.course1.cloudstorage;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;

public class MyErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError() {
        return "error";
    }
    @Override
    public String getErrorPath() {
        return "/error";
    }
}
