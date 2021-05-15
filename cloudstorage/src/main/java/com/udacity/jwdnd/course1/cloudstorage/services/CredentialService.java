package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CredentialService {
    private final UserService userService;
    private final CredentialMapper credentialMapper;
    private final EncryptionService encryptionService;


    public CredentialService(UserService userService, CredentialMapper credentialMapper, EncryptionService encryptionService) {
        this.userService = userService;
        this.credentialMapper = credentialMapper;
        this.encryptionService = encryptionService;
    }

    public String encryptKey(Credential credential) {
        return this.encryptionService.encryptValue(credential.getPassword(), credential.getKey());
    }

    public List<Credential> getCredentialOfUser(Integer userId) {
        List<Credential> files = credentialMapper.getCredentialByUserId(userId);
        return files;
    }

    public int create(Credential credential) {
        String key = this.encryptionService.generateKey();
        credential.setKey(key);
        credential.setPassword(this.encryptKey(credential));
        return credentialMapper.create(credential);
    }

    public void update(Credential credential) {
        Credential retrievedCredential = this.credentialMapper.retrieveKeyByCredentialId(credential.getCredentialId());
        credential.setKey(retrievedCredential.getKey());
        String encodedPass = this.encryptionService.encryptValue(credential.getPassword(), credential.getKey());
        credential.setPassword(encodedPass);
        credentialMapper.update(credential);
    }


    public Credential getById(Integer credentialId) {
        return this.credentialMapper.getById(credentialId);
    }

    public void delete(Integer credentialId) {
        credentialMapper.delete(credentialId);
    }

    public Credential retrieveKeyByCredentialId(Integer credentialId) {
        return this.credentialMapper.retrieveKeyByCredentialId(credentialId);
    }


}
