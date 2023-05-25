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
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, SignatureException, IOException {
        PublicKey pb = null;
        PrivateKey pk = null;
        Documento doc = new Documento();
        Assinatura assinatura = new Assinatura();
        Scanner sc = new Scanner(System.in);
        ControladorSenha cs = new ControladorSenha();

        System.out.println("BEM VINDO!");
        int opc = 0;
        do {
            System.out.println("Digite 1 para assinar um PDF," +
                    "\nDigite 2 para criar as senhas" +
                    "\nDigite 3 para pegar as senhas do computador" +
                    "\nDigite 4 para verificar assinatura");
            opc = sc.nextInt();

            switch (opc){
                case 1:
                    if(pk == null || pb == null){
                        System.out.println("Você ainda não tem chaves salvas!!");
                        break;
                    }
                    else{
                        System.out.println("Digite seu nome: ");
                        String nome = sc.next();
                        Path pdf = doc.pegarPDF();
                        assinatura.assinarPdf(pdf.toAbsolutePath(),pk,nome);
                    }
                    break;
                case 2:
                    System.out.println("Escolha a pasta para salvar as senhas.");
                    cs.criarSenhas();
                    break;
                case 3:
                    pk = cs.senhaPrivada();
                    pb = cs.senhaPublica();
                    if (pk != null && pb != null){
                        System.out.println("Senhas foram armazenadas para uso!");
                    }
                    break;
            }
        }while (opc >0);


    }
}


