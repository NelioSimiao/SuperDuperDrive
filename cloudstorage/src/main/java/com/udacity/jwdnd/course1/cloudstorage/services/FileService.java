package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class FileService {

    private final FileMapper fileMapper;
    private final UserService userService;

    public FileService(FileMapper userMapper, UserService userService) {
        this.fileMapper = userMapper;
        this.userService = userService;
    }

    public boolean isFileNameAvailable(String fileName) {
        return fileMapper.getFile(fileName) != null;
    }

    public List<File> getFilesForUser(Integer userId) {
        List<File> files = fileMapper.getFilesByUserId(userId);
        return files;
    }


    public int createFile(MultipartFile file,Integer userId) throws IOException {
        File files = new File(null, file.getOriginalFilename(), file.getContentType(), file.getSize(), userId, file.getBytes());
        return fileMapper.insert(files);
    }

    public File getFileById(Integer id) {
        return this.fileMapper.getFileById(id);
    }

    public void delete(Integer fileId) {
        fileMapper.delete(fileId);
    }

}
