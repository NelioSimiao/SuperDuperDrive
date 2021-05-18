package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.constants.Constants;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLConnection;

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
            redirectAttributes.addAttribute("message", Constants.SELECT_FILE);
            return "redirect:/home";
        }
        if(fileService.isFileNameAvailable(file.getOriginalFilename())){
            redirectAttributes.addAttribute("error", true);
            redirectAttributes.addAttribute("message", Constants.FILE_ALREADY_EXISTS);
            return "redirect:/home";
        };

        try {
            fileService.createFile(file,user.getUserId());
        } catch (IOException e) {
            redirectAttributes.addAttribute("error", true);
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/home";
        }
        redirectAttributes.addAttribute("success", true);
        redirectAttributes.addAttribute("message", file.getOriginalFilename().concat(Constants.SUCCESSFULLY_CREATED));

        return "redirect:/home";
    }

    @GetMapping("home/file/view/{fileId}")
    public void view(HttpServletRequest request, HttpServletResponse response, Model model,@PathVariable("fileId") Integer fileId) throws IOException {
        File fileFromDataBase = fileService.getFileById(fileId);
        java.io.File file = new java.io.File(".\\");
        try(FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(fileFromDataBase.getFileData());

        } catch (IOException e) {
            model.addAttribute("success", true);
            model.addAttribute("message", Constants.UNESPECTED_ERROR);
        }

        if (file.exists()) {
            //get the mimetype
            String mimeType = URLConnection.guessContentTypeFromName(file.getName());
            if (mimeType == null) {
                //unknown mimetype so set the mimetype to application/octet-stream
                mimeType = "application/octet-stream";
            }
            String contentType = fileFromDataBase.getContentType();
            response.setContentType(mimeType);

            /**
             * In a regular HTTP response, the Content-Disposition response header is a
             * header indicating if the content is expected to be displayed inline in the
             * browser, that is, as a Web page or as part of a Web page, or as an
             * attachment, that is downloaded and saved locally.
             *
             */
            /**
             * Here we have mentioned it to show inline
             */
            response.setHeader("Content-Disposition", String.format("inline; filename=\"" + fileFromDataBase.getFileName() + "\""));
            //Here we have mentioned it to show as attachment
            // response.setHeader("Content-Disposition", String.format("attachment; filename=\"" + file.getName() + "\""));
            response.setContentLength((int) file.length());
            InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
            FileCopyUtils.copy(inputStream, response.getOutputStream());
        }
    }


    @GetMapping("home/file/delete/{fileId}")
    public String delete(@PathVariable("fileId") Integer fileId,RedirectAttributes redirectAttributes) {
        String fileName = fileService.getFileById(fileId).getFileName();

        fileService.delete(fileId);
        redirectAttributes.addAttribute("success", true);
        redirectAttributes.addAttribute("message", fileName.concat(Constants.SUCCESSFULLY_DELETED));

        return "redirect:/home";

    }

}
