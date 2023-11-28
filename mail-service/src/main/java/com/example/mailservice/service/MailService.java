package com.example.mailservice.service;

import com.example.mailservice.entity.MailStructure;

public interface MailService {
    void sendMail(String mail, MailStructure mailStructure);
}
