package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CredentialController {

    private final CredentialService credentialService;
    private final UserService userService;

    public CredentialController(CredentialService credentialService, UserService userService) {
        this.credentialService = credentialService;
        this.userService = userService;
    }

    @PostMapping("home/credentials")
    public String create(@ModelAttribute Credential credential, Authentication auth,
                         RedirectAttributes redirectAttributes) {
        Integer userId = userService.getUser(auth.getName()).getUserId();


        try {
            if (credential.getCredentialId() == null) {
                credential.setUserid(userId);
                credentialService.create(credential);
                redirectAttributes.addAttribute("success", true);

                redirectAttributes.addAttribute("message",
                        "Credential create ".concat(credential.getUsername()));
            } else {
                credentialService.update(credential);
                redirectAttributes.addAttribute("success", true);

                redirectAttributes.addAttribute("message",
                        "Credential  was edited ".concat(credential.getUsername()));
            }
        } catch (Exception e) {
            redirectAttributes.addAttribute("error", true);

            redirectAttributes.addAttribute("message", e.getMessage());
            System.out.println(e.getMessage());
        }
        return "redirect:/home";
    }

    @GetMapping("home/credentials/delete/{credentialId}")
    public String delete(@PathVariable("credentialId") Integer credentialId,
                         RedirectAttributes redirectAttributes) {
        Credential credential = credentialService.getById(credentialId);
        String username = credential.getUsername();

        try {
            credentialService.delete(credential.getCredentialId());
            redirectAttributes.addAttribute("success", true);
            redirectAttributes.addAttribute("message",
                    "CredentialId deleted ".concat(username));

        } catch (Exception e) {
            redirectAttributes.addAttribute("error", true);

            redirectAttributes.addAttribute("message",
                    "Unexpected error deleting the file ".concat(username));
        }
        return "redirect:/home";

    }

}
