package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
public class FileController {

    private final FileService fileService;
    private final UserService  userService;


    public FileController(FileService fileService, UserService userService) {
        this.fileService = fileService;
        this.userService = userService;
    }


    @PostMapping("/upload")
    public String upload(@RequestParam("fileUpload") MultipartFile file, RedirectAttributes redirectAttributes, Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        User user = userService.getUser(currentPrincipalName);

        // check if file is empty
        if (file.isEmpty()) {
            redirectAttributes.addAttribute("error", true);
            redirectAttributes.addAttribute("message", "Please select a file to upload.");
            return "redirect:/home";
        }
        if(fileService.isFileNameAvailable(file.getOriginalFilename())){
            redirectAttributes.addAttribute("error", true);
            redirectAttributes.addAttribute("message", "File name already upLoaded.");
            return "redirect:/home";
        };

        try {
            fileService.createFile(file,user.getUserId());
        } catch (Exception e) {
            redirectAttributes.addAttribute("error", true);
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/home";
        }
        redirectAttributes.addAttribute("success", true);
        redirectAttributes.addAttribute("message", "You successfully uploaded ".concat(file.getOriginalFilename()));

        return "redirect:/home";
    }

    @GetMapping("home/file/view/{fileId}")
    public ResponseEntity view(@PathVariable("fileId") Integer fileId) {
        File file = fileService.getFileById(fileId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\""+file.getFileName()+"\"")
                .body(file.getFileData());

    }


    @GetMapping("home/file/delete/{fileId}")
    public String delete(@PathVariable("fileId") Integer fileId,RedirectAttributes redirectAttributes) {
        String fileName = fileService.getFileById(fileId).getFileName();

        try {
            fileService.delete(fileId);
            redirectAttributes.addAttribute("success", true);
            redirectAttributes.addAttribute("message",
                    "File deleted ".concat(fileName));

        } catch (Exception e) {
            redirectAttributes.addAttribute("error", true);
            redirectAttributes.addAttribute("message",
                    "Unexpected error deleting the file ".concat(fileName));
        }

        return "redirect:/home";

    }

}
