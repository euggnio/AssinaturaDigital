package com.codigo;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.PDSignature;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.*;
import java.util.Calendar;
import java.util.Date;

public class Assinatura {

    public Documento doc = new Documento();

    public void assinarPdf(Path documento, PrivateKey chavePrivada,String nome){
        try {
            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
            byte[] pdfHash = sha256.digest(Files.readAllBytes(documento));

            Signature assinar = Signature.getInstance("DSA");
            assinar.initSign(chavePrivada);
            assinar.update(pdfHash);
            byte[] assinatura = assinar.sign();

            PDDocument pdf = PDDocument.load(documento.toFile());

            PDSignature pdSignature = new PDSignature();
            pdSignature.setFilter(PDSignature.FILTER_ADOBE_PPKLITE);
            pdSignature.setSubFilter(PDSignature.SUBFILTER_ADBE_PKCS7_DETACHED);
            pdSignature.setName(nome);
            pdSignature.setReason("Aula de segurança");
            pdSignature.setLocation("Brasil");
            Calendar signDate = Calendar.getInstance();
            signDate.setTime(new Date());
            pdSignature.setContactInfo("(51) 3247-8400");
            pdSignature.setSignDate(signDate);
            pdSignature.setContents(assinatura);

            pdf.addSignature(pdSignature);
            String caminhoParaSalvar = doc.pegarDiretorio().toString();
            pdf.save(caminhoParaSalvar);
            System.out.println("Sera salvo em: " + caminhoParaSalvar);
            pdf.close();
            System.out.println("PDF foi assinado e salvo com sucesso!!");
        } catch (NoSuchAlgorithmException | IOException | SignatureException | InvalidKeyException e) {
            System.out.println("Ocorreu algum erro: " + e.getMessage());
        }

    }

}
