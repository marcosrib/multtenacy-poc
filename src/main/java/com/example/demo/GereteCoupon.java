package com.example.demo;

import org.springframework.core.io.ClassPathResource;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class GereteCoupon {
    private static final String RESOURCES_PATH = "static/";
    public byte[] excecute(Comprovate comprovate, String imageName) throws IOException {
        int width = 500;
        int height = 300;
        int spacing = 30;
        int textSpacing = 5;
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = bufferedImage.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Set background color and fill
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, width, height);

        // Draw the logo
        if (imageName != null && !imageName.isEmpty()) {
            ClassPathResource imgFile = new ClassPathResource(RESOURCES_PATH + imageName);
            InputStream logoStream = imgFile.getInputStream();
            BufferedImage logo = ImageIO.read(logoStream);

            Image scaledLogo = logo.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            g2d.drawImage(scaledLogo, 10, 10, null);
        }


        // Draw the text
        // Draw the text
        g2d.setColor(Color.BLACK);
        Font labelFont = new Font("Arial", Font.BOLD, 16);
        Font valueFont = new Font("Arial", Font.PLAIN, 12);

        int yPosition = 130; // Posição inicial y para o texto

        g2d.setFont(labelFont);
        g2d.drawString("Comprovante de Pagamento", 150, 50);


        yPosition = drawLabelValuePair(g2d, "Nome do Cliente:", comprovate.name(), 10, yPosition, labelFont, valueFont, textSpacing, spacing);
        yPosition = drawLabelValuePair(g2d, "Data do Pagamento:", comprovate.cpf(), 10, yPosition, labelFont, valueFont, textSpacing, spacing);



        g2d.dispose();

        // Convert BufferedImage to byte array
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "jpeg", byteArrayOutputStream);

        File file = new File("/home/marcos-pc/Downloads/log.jpeg");
        ImageIO.write(bufferedImage, "jpeg", file);
        return byteArrayOutputStream.toByteArray();

    }
    private int drawLabelValuePair(Graphics2D g2d, String label, String value, int x, int y, Font labelFont, Font valueFont, int textSpacing, int spacing) {
        FontRenderContext frc = g2d.getFontRenderContext();

        // Draw label
        g2d.setFont(labelFont);
        TextLayout labelLayout = new TextLayout(label, labelFont, frc);
        labelLayout.draw(g2d, x, y);

        // Calculate the y position for the value
        int valueYPosition = y + (int) labelLayout.getBounds().getHeight() + textSpacing;

        // Draw value below the label
        g2d.setFont(valueFont);
        TextLayout valueLayout = new TextLayout(value, valueFont, frc);
        valueLayout.draw(g2d, x, valueYPosition);

        // Return new y position for the next pair
        return valueYPosition + (int) valueLayout.getBounds().getHeight() + spacing;
    }
}
