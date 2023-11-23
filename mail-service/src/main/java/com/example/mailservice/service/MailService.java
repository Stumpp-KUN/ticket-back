package com.example.mailservice.service;

import com.example.mailservice.entity.MailStructure;
import com.example.mailservice.event.MailEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MailService {

    private final JavaMailSender javaMailSender;
    private final String fromMail;

    @KafkaListener(topics = "mailTopic")
    public void listen(MailEvent mailEvent) {
        log.info("Got ticket to sent {}",mailEvent);

        sendMail(mailEvent.getMail(),new MailStructure(mailEvent.getSubject(),mailEvent.getMessage()));
    }

    @Autowired
    public MailService(@Value("${spring.mail.username}") String fromMail, JavaMailSender javaMailSender){
        this.fromMail=fromMail;
        this.javaMailSender=javaMailSender;
    }

    public void sendMail(String mail, MailStructure mailStructure){
        SimpleMailMessage simpleMailMessage=new SimpleMailMessage();
        simpleMailMessage.setFrom(fromMail);
        simpleMailMessage.setSubject(mailStructure.getSubject());
        simpleMailMessage.setText(mailStructure.getMessage());
        simpleMailMessage.setTo(mail);

        javaMailSender.send(simpleMailMessage);

    }
}