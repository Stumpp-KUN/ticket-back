package com.example.mailservice.controller;

import com.example.mailservice.entity.MailStructure;
import com.example.mailservice.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mail")
@RequiredArgsConstructor
public class MailController {

    private final MailService mailService;

    @PostMapping("/send/{mail}")
    public ResponseEntity<String> sendMail(@PathVariable String mail, @RequestBody MailStructure mailStructure){
            mailService.sendMail(mail,mailStructure);
           return ResponseEntity.ok("Successfully sended");
    }
}
