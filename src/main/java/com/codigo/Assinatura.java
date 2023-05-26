package com.codigo;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.PDSignature;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;

public class Assinatura {

    public Documento doc = new Documento();

    public void assinarPdf(Path documento, PrivateKey chavePrivada,String nome){
        try {
            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
            byte[] pdfHash = sha256.digest(Files.readAllBytes(documento));


            Signature assinar = Signature.getInstance("SHA256withRSA");
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
            pdf.save(doc.pegarDiretorio().toString() + "\\" + documento.getFileName());
            pdf.close();

            System.out.println("PDF foi assinado e salvo com sucesso!!");
        } catch (NoSuchAlgorithmException | IOException | SignatureException | InvalidKeyException e) {
            System.out.println("Ocorreu algum erro: " + e.getMessage());
        }

    }

    public void verificarAssinatura(Path documento, PublicKey chavePublica) {
        try {
            PDDocument pdDocument = PDDocument.load(documento.toFile());
            PDSignature signature = pdDocument.getLastSignatureDictionary();
            byte[] assinatura = signature.getContents();

            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
            byte[] pdfHash = sha256.digest(pdDocument.getSignatureDictionaries().get(0).getSignedContent(Files.readAllBytes(documento)));




            Signature verificar = Signature.getInstance("SHA256withRSA");
            verificar.initVerify(chavePublica);
            verificar.update(pdfHash);


            if (verificar.verify(assinatura)) {
                System.out.println("Assinatura foi feita corretamente.");
            } else {
                System.out.println("Assinatura errada ou inexistente.");
            }
        } catch (NoSuchAlgorithmException | InvalidKeyException | IOException | SignatureException e) {
            System.out.println("Erro ao verificar a assinatura: " + e.getMessage());
            e.printStackTrace();

    }
    }

}
