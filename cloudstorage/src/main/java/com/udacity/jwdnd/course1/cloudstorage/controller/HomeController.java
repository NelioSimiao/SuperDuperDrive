package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/home")
public class HomeController {
    private final FileService fileService;
    private final UserService userService;
    private final NoteService noteService;
    private final CredentialService credentialService;
    @Autowired
    private  EncryptionService encryptionService;

    public HomeController(FileService fileService, UserService userService, NoteService noteService, CredentialService credentialService) {
        this.fileService = fileService;
        this.userService = userService;
        this.noteService = noteService;
        this.credentialService = credentialService;
    }

    @GetMapping()
    public String homeView(Model model) {
        Integer userId = userService.getLogedUser().getUserId();
        List<File> files = this.fileService.getFilesForUser(userId);
        model.addAttribute("files", files);
        List<Note> notes = this.noteService.getNotesOfUser(userId);
        model.addAttribute("notes", notes);
        List<Credential> credentials = this.credentialService.getCredentialOfUser(userId);
        model.addAttribute("credentials", credentials);

        model.addAttribute("encryptionService", this.encryptionService);
        return "home";
    }
}
