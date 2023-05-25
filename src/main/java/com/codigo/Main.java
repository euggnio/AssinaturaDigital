package com.codigo;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.PDSignature;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.util.Calendar;
import java.util.Date;

public class Main {
    public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, SignatureException, IOException {

            ControladorSenha cs = new ControladorSenha();

            PDDocument pd = PDDocument.load(cs.documento.pegarPDF().toFile());
        System.out.println("TITULO: " + pd.getDocumentInformation().getTitle());

            cs.criarSenhas();

            PrivateKey pk = cs.senhaPrivada();
            PublicKey pb = cs.senhaPublica();

        System.out.println(pk.getEncoded());


    }
}


