package kr.manyofactory.manyoshop.helpers;

import java.io.UnsupportedEncodingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class MailHelper {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${mailhelper.sender.name}")
    private String senderName;

    @Value("${mailhelper.sender.email}")
    private String senderEmail;

    public void sendMail(String receiverName, String receiverEmail, String subject, String content)
            throws Exception {
        log.debug("-------------------");
        log.debug(String.format("RecvName: %s", receiverName));
        log.debug(String.format("RecvEmail: %s", receiverEmail));
        log.debug(String.format("Subject: %s", subject));
        log.debug("-------------------");

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            helper.setSubject(subject);
            helper.setText(content, true);
            helper.setTo(new InternetAddress(receiverEmail, receiverName, "UTF-8"));
            helper.setFrom(new InternetAddress(senderEmail, senderName, "UTF-8"));

            javaMailSender.send(message);
        } catch (MessagingException e) {
            log.error("메일 발송 정보 설정 실패", e);
            throw e;
        } catch (UnsupportedEncodingException e) {
            log.error("지원하지 않는 인코딩", e);
            throw e;
        } catch (Exception e) {
            log.error("알 수 없는 오류", e);
            throw e;
        }
    }

    public void sendMail(String receiverEmail, String subject, String content) throws Exception {
        this.sendMail(null, receiverEmail, subject, content);
    }
}
