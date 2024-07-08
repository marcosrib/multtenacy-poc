package com.example.demo;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;


@RestController
public class CouponController {

    private final CouponRepository repository;
    private final ReceiptPdfService receiptPdfService;
   private final JavaMailSender javaMailSender;
    public CouponController(CouponRepository repository, ReceiptPdfService receiptPdfService, JavaMailSender javaMailSender) {
        this.repository = repository;
        this.receiptPdfService = receiptPdfService;
        this.javaMailSender = javaMailSender;
    }

    @GetMapping("/cupon")
    public List<Coupon> getCoupons() {

        return repository.findAll();
    }

    @PostMapping("/cupon")
    public Coupon create(@RequestBody Coupon coupon) throws MessagingException {
        System.out.println(coupon);
        var pdfBytes = receiptPdfService.generatePdf(new Comprovate("tste",  "90345uju90u9"));
        String subject = "Your Payment Receipt";
        String text = "Dear customer, please find attached your payment receipt.";
        String pdfFilename = "receipt.pdf";

        sendEmailWithAttachment("marcostsest@gmail.com", subject, text, pdfBytes, pdfFilename);

        return null;
        //return repository.save(coupon);
    }

    private void sendEmailWithAttachment(String to, String subject, String text, byte[] pdfBytes, String pdfFilename) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text);
        helper.addAttachment(pdfFilename, new ByteArrayResource(pdfBytes));

        javaMailSender.send(message);
    }
}
