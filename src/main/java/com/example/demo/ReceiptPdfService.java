package com.example.demo;

import com.itextpdf.html2pdf.HtmlConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
public class ReceiptPdfService {


    private static final Logger log = LoggerFactory.getLogger(ReceiptPdfService.class);

    public byte[] generatePdf(Comprovate comprovate) {
        var htmlContent = getTemplateHtml();
        if (htmlContent.isEmpty()) return new byte[0];
        htmlContent = htmlContent.replace("{{customerName}}", comprovate.name());
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        HtmlConverter.convertToPdf(htmlContent, byteArrayOutputStream);
        try (FileOutputStream fos = new FileOutputStream("/home/marcos-pc/Downloads/pdf-receipt.pdf")) {
            fos.write(byteArrayOutputStream.toByteArray());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return byteArrayOutputStream.toByteArray();
    }

    private String getTemplateHtml() {
        ClassPathResource resource = new ClassPathResource("/templates/receipt.html");
        String htmlContent = null;

        try {
            htmlContent = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            log.error("Error get html pdf template: {} ", e.getLocalizedMessage());
        }

        return htmlContent;
    }
}
