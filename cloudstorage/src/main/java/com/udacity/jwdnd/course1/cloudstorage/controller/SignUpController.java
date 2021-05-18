package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.constants.Constants;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/signup")
public class SignUpController {

    private final UserService userService;
    public SignUpController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public String signupView() {
        return "signup";
    }

    @PostMapping()
    public String signupUser(@ModelAttribute User user, Model model,  RedirectAttributes redirectAttrs )
{
        String signupError = null;

        if (!userService.isUsernameAvailable(user.getUserName())) {
            signupError = Constants.EXISTING_USER_ERROR;
        }

        if (signupError == null) {
            int rowsAdded = userService.createUser(user);
            if (rowsAdded < 0) {
                signupError = Constants.ERROR_SIGN_UP;
            }
        }

        if (signupError == null) {
            redirectAttrs.addFlashAttribute("signupSucess", Constants.SIGNUP_SUCCESS);

            return "redirect:/login";

        } else {
            model.addAttribute("signupError", signupError);
            return "signup";
        }
    }


}
