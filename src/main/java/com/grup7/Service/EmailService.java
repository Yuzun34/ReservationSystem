/**
 * E-posta gönderimi işlemlerini gerçekleştiren servis sınıfı.
 * Rezervasyon onayları, bilgilendirme mesajları gibi
 * e-postaların kullanıcılara gönderilmesini sağlar.
 * Spring'in JavaMailSender arayüzünü kullanır.
 */

package com.grup7.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmailWithAttachment(String to, String subject, String body, String filePath, String fileName) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(body);

        FileSystemResource file = new FileSystemResource(new File(filePath));
        helper.addAttachment(fileName, file);

        mailSender.send(message);
    }
}