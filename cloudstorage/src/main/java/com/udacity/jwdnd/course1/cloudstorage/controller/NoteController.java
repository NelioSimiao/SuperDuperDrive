package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.constants.Constants;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
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

import java.util.List;

@Controller
public class NoteController {

    private final NoteService noteService;
    private final UserService userService;

    @Autowired
    public NoteController(NoteService noteService, UserService userService) {
        this.noteService = noteService;
        this.userService = userService;
    }

    @PostMapping("home/notes")
    public String create(@ModelAttribute Note note, Authentication auth,
                         RedirectAttributes redirectAttributes) {
        note.setUserId(userService.getUser(auth.getName()).getUserId());
       if( noteService.isNoteNameAvailable(note.getNoteDescription())){
           redirectAttributes.addAttribute("success", true);
           redirectAttributes.addAttribute("message", Constants.NOTE_WITH_SAME_DESCRIPTION_ALREADY_EXISTS);
           return "redirect:/home";
       }

        try {
            noteService.create(note);
            redirectAttributes.addAttribute("success", true);
            redirectAttributes.addAttribute("message", Constants.NOTE_SUCCESSFULLY_CREATED);
        } catch (Exception e) {
            redirectAttributes.addAttribute("error", true);
            redirectAttributes.addAttribute("messageError", Constants.UNESPECTED_ERROR);
            System.out.println(e.getMessage());

        }
        return "redirect:/home";
    }

    @PostMapping("home/notes/edit")
    public String edit(@ModelAttribute Note note, Authentication auth,
                       RedirectAttributes redirectAttributes) {

        if( noteService.isNoteNameAvailable(note.getNoteDescription())){
            redirectAttributes.addAttribute("success", true);
            redirectAttributes.addAttribute("message", Constants.NOTE_WITH_SAME_DESCRIPTION_ALREADY_EXISTS);
            return "redirect:/home";
        }
        try {
            noteService.update(note);
            redirectAttributes.addAttribute("success", true);
            redirectAttributes.addAttribute("message", Constants.NOTE_SUCCESSFULLY_EDITED);
        } catch (Exception e) {
            redirectAttributes.addAttribute("error", true);
            redirectAttributes.addAttribute("messageError", Constants.UNESPECTED_ERROR);
            System.out.println(e.getMessage());
        }
        return "redirect:/home";
    }

    @GetMapping("home/notes/delete/{noteId}")
    public String delete(@PathVariable("noteId") Integer noteId, RedirectAttributes redirectAttributes) {
        String title = noteService.getById(noteId).getNoteTitle();

        try {
            noteService.delete(noteId);
            redirectAttributes.addAttribute("success", true);

            redirectAttributes.addAttribute("message",
                    Constants.NOTE_SUCCESSFULLY_DELETED);

        } catch (Exception e) {
            redirectAttributes.addAttribute("error", true);

            redirectAttributes.addAttribute("messageError",
                    Constants.UNESPECTED_ERROR);
        }
        return "redirect:/home";

    }

}
