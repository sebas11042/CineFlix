package com.cineflix.service;

import org.springframework.stereotype.Service;

@Service
public class EmailService {

  //  @Autowired
   // private JavaMailSender mailSender;

 /*   public void enviarCorreoConPDF(String destinatario, String asunto, byte[] pdfAdjunto) {
        try {
            MimeMessage mensaje = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mensaje, true);
            helper.setTo(destinatario);
            helper.setSubject(asunto);
            helper.setText("Gracias por tu compra en CineFlix. Adjuntamos tu comprobante en PDF.");

            helper.addAttachment("reserva.pdf", new ByteArrayResource(pdfAdjunto));

            mailSender.send(mensaje);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
        */
}
