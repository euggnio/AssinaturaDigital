package com.codigo;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.PDSignature;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.nio.file.Path;
import java.security.*;
import java.util.Base64;
import java.util.Scanner;
import java.util.Set;

public class Main {
    public static void main(String[] args){
        PublicKey pb = null;
        PrivateKey pk = null;
        Documento doc = new Documento();
        Assinatura assinatura = new Assinatura();
        Scanner sc = new Scanner(System.in);
        ControladorSenha cs = new ControladorSenha();

        Provider[] providers = Security.getProviders();
        for (Provider provider : providers) {
            Set<Provider.Service> services = provider.getServices();
            for (Provider.Service service : services) {
                if (service.getType().equals("Signature")) {
                    System.out.println(service.getAlgorithm());
                }
            }
        }

        System.out.println("BEM VINDO!");
        int opc = 0;
        do {
            System.out.println("Digite 1 para assinar um PDF." +
                    "\nDigite 2 para criar as senhas." +
                    "\nDigite 3 para carregar suas senhas." +
                    "\nDigite 4 para verificar assinatura.");
            opc = sc.nextInt();

            switch (opc){
                case 1:
                    if(pk == null || pb == null){
                        System.out.println("Você ainda não tem chaves carregadas!!");
                        break;
                    }
                    else{
                        System.out.println("Digite seu nome: ");
                        String nome = sc.next();
                        Path pdf = doc.pegarPDF();
                        assinatura.assinarPdf(pdf.toAbsolutePath(),pk,nome);
                        break;
                    }
                case 2:
                    System.out.println("Escolha a pasta para salvar as senhas.");
                    cs.criarSenhas();
                    break;
                case 3:
                    pk = cs.senhaPrivada();
                    pb = cs.senhaPublica();

                    System.out.println("Publica: " + Base64.getEncoder().encodeToString(pb.getEncoded()));
                    System.out.println("Privada: " + Base64.getEncoder().encodeToString(pk.getEncoded()));

                    if (pk != null && pb != null){
                        System.out.println("Senhas foram armazenadas para uso!");
                    }
                    break;
                case 4:
                    if (pk == null || pb == null){
                        System.out.println("Você ainda não tem chaves carregadas!!");
                        break;
                    }else{
                        Path pdf = doc.pegarPDF();
                        assinatura.verificarAssinatura(pdf.toAbsolutePath(),pb);
                    }
            }
        }while (opc >0);


    }
}


