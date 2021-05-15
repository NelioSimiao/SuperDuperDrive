package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class NoteService {

    private final NoteMapper noteMapper;
    private final UserService userService;

    public NoteService(NoteMapper noteMapper, UserService userService) {
        this.noteMapper = noteMapper;
        this.userService = userService;
    }


    public boolean isNoteNameAvailable(String noteDescription) {
        return noteMapper.getNoteDescription(noteDescription) != null;

    }

    public List<Note> getNotesOfUser(Integer userId) {
        List<Note> notes = noteMapper.getNotesByUserId(userId);
        return notes;
    }


    public int create( Note note) throws IOException {
        return noteMapper.insert(note);
    }

    public Note getById(Integer id) {
        return this.noteMapper.getById(id);
    }

    public void delete(Integer fileId) {
        noteMapper.delete(fileId);
    }

    public void update( Note note) throws IOException {
         noteMapper.update(note);
    }

}
