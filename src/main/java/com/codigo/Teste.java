package com.codigo;

import org.apache.pdfbox.cos.COSString;
import org.apache.pdfbox.pdmodel.PDDocument;

import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.pdmodel.common.PDStream;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.PDSignature;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.SignatureOptions;
import org.apache.pdfbox.text.PDFTextStripper;


import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.*;


public class Teste {
    public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, SignatureException, IOException, KeyStoreException, CertificateException, UnrecoverableEntryException {


        Path keystoreFile= Paths.get("C:\\Users\\eugen\\Desktop\\TESTE\\keystore.p12");
        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(keystoreFile.toUri().toURL().openStream(), "eugenio".toCharArray());



        KeyStore.PasswordProtection passwordProtection = new KeyStore.PasswordProtection("eugenio".toCharArray());
        KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry("myalias",passwordProtection);
        PrivateKey privateKey = privateKeyEntry.getPrivateKey();

        System.out.println("PUBLIC key :: " + privateKey.getEncoded());

        Path certFile= Paths.get("C:\\Users\\eugen\\Desktop\\TESTE\\certificate.crt");
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        Certificate cert = cf.generateCertificate(certFile.toUri().toURL().openStream());

        System.out.println("Cert pb key:: " + cert.getPublicKey().getEncoded());

        Path pdfFile= Paths.get("C:\\Users\\eugen\\Downloads\\eugenio.pdf");
        Path pdfFileSaida= Paths.get("C:\\Users\\eugen\\Desktop\\TESTE\\eugenio.pdf");
        PDDocument document = PDDocument.load(pdfFile.toFile());
        PDFTextStripper stripper = new PDFTextStripper();
        String text = stripper.getText(document);


        MessageDigest sha256 = MessageDigest.getInstance("SHA-256");

        byte[] pdfHash = sha256.digest(text.getBytes());

        Signature signature = Signature.getInstance("SHA256WithRSA");
        signature.initSign(privateKey);
        signature.update(pdfHash);
        byte[] assinatura = signature.sign();


        PDSignature ass = new PDSignature();
        ass.setFilter(PDSignature.FILTER_ADOBE_PPKLITE);
        ass.setSubFilter(PDSignature.SUBFILTER_ADBE_PKCS7_DETACHED);
        ass.setName("Teste");
        ass.setLocation("LocalAssinatura");
        ass.setReason("MotivoAssinatura");

        PDPageTree pdPage = document.getPages();
        pdPage.get(0).getContents();


        SignatureOptions signatureOptions = new SignatureOptions();
        signatureOptions.setPreferredSignatureSize(256);
        document.addSignature(ass,signatureOptions);

        COSString certCosString = new COSString(cert.getEncoded());
        COSString hash = new COSString(Base64.getEncoder().encode(assinatura));
        ass.getCOSObject().setItem("Cert", certCosString);
        ass.getCOSObject().setItem("Signature", hash);


        OutputStream out = new FileOutputStream("C:\\Users\\eugen\\Desktop\\TESTE\\eugenio.pdf");
        document.save(out);
        document.close();

        Path pdfV = Paths.get("C:\\Users\\eugen\\Desktop\\TESTE\\eugenio.pdf");
        PDDocument documentV = PDDocument.load(pdfV.toFile());
        PDFTextStripper stripperV = new PDFTextStripper();
        String textV = stripperV.getText(documentV);
        byte[] pdfHashV = sha256.digest(textV.getBytes());

        Signature signatureV = Signature.getInstance("SHA256WithRSA");
        signatureV.initVerify(cert);
        signatureV.update(pdfHashV);

        if(documentV.getLastSignatureDictionary() == null){
            System.out.println("Documento não assinado.");
        }else {
            byte[] assinaturaV = Base64.getDecoder().decode(documentV.getLastSignatureDictionary().getCOSObject().getString("Signature"));

            if (signatureV.verify(assinaturaV)) {
                System.out.println("true");
            } else {
                System.out.println("false");
            }
        }












}}


