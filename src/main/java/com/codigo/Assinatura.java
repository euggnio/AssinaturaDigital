package com.codigo;

import org.apache.pdfbox.cos.COSString;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.PDSignature;
import org.apache.pdfbox.text.PDFTextStripper;

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
            PDDocument pdf = PDDocument.load(documento.toFile());
            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(pdf);
            byte[] pdfHash = sha256.digest(text.getBytes());

            Signature assinar = Signature.getInstance("SHA256withRSA");
            assinar.initSign(chavePrivada);
            assinar.update(pdfHash);
            byte[] assinatura = assinar.sign();



            PDSignature pdSignature = new PDSignature();
            pdSignature.setFilter(PDSignature.FILTER_ADOBE_PPKLITE);
            pdSignature.setSubFilter(PDSignature.SUBFILTER_ADBE_PKCS7_DETACHED);
            pdSignature.setName(nome);
            pdSignature.setReason("Aula de segurança");
            pdSignature.setLocation("Brasil");
            pdSignature.setContactInfo("(51) 3247-8400");
            pdSignature.setSignDate(Calendar.getInstance());
            pdSignature.setContents(assinatura);

            COSString hash = new COSString(Base64.getEncoder().encode(assinatura));
            pdSignature.getCOSObject().setItem("Signature", hash);

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

            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
            PDFTextStripper stripperV = new PDFTextStripper();
            String textV = stripperV.getText(pdDocument);
            byte[] pdfHash = sha256.digest(textV.getBytes());


            Signature signatureV = Signature.getInstance("SHA256WithRSA");
            signatureV.initVerify(chavePublica);
            signatureV.update(pdfHash);

            if (pdDocument.getLastSignatureDictionary() == null) {
                System.out.println("Documento não assinado.");
            } else {
                byte[] assinaturaV = Base64.getDecoder().decode(pdDocument.getLastSignatureDictionary().getCOSObject().getString("Signature"));

                if (signatureV.verify(assinaturaV)) {
                    System.out.println("Documento assinado!");
                } else {
                    System.out.println("Documento não assinado");
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (SignatureException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        }

    }}
