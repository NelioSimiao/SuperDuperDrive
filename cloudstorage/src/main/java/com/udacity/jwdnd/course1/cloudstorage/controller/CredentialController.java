package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.constants.Constants;
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

        Credential credential1 = credentialService.retrieveByUserName(credential.getUsername());

        if (credential1 != null) {
            redirectAttributes.addAttribute("success", true);
            redirectAttributes.addAttribute("message",
                    Constants.EXISTING_USER_ERROR);
            return "redirect:/home";
        }
        try {
            if (credential.getCredentialId() == null) {
                credential.setUserid(userId);
                credentialService.create(credential);
                redirectAttributes.addAttribute("success", true);

                redirectAttributes.addAttribute("message",
                        Constants.CREDENTIAL_SUCCESSFULLY_CREATED);
            } else {
                credentialService.update(credential);
                redirectAttributes.addAttribute("success", true);

                redirectAttributes.addAttribute("message", Constants.CREDENTIAL_SUCCESSFULLY_EDITED);
            }
        } catch (Exception e) {
            redirectAttributes.addAttribute("error", true);

            redirectAttributes.addAttribute("message", Constants.UNESPECTED_ERROR);
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
                    Constants.CREDENTIAL_SUCCESSFULLY_DELETED);

        } catch (Exception e) {
            redirectAttributes.addAttribute("error", true);

            redirectAttributes.addAttribute("message",
                    Constants.UNESPECTED_ERROR);
        }
        return "redirect:/home";

    }

}
