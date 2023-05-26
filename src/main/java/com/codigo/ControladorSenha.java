package com.codigo;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.*;
import java.util.Base64;

public class ControladorSenha {

    public Documento documento = new Documento();


    public PrivateKey senhaPrivada(){
        try {
            Path chavePrivada = documento.pegarKey("CHAVE PRIVADA");
            byte[] chavePrivadaBytes = Files.readAllBytes(chavePrivada);
            EncodedKeySpec encoder = new PKCS8EncodedKeySpec(chavePrivadaBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey pk = keyFactory.generatePrivate(encoder);

            return pk;
        }
        catch (IOException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    public PublicKey senhaPublica(){
        try {
            Path chavePublica = documento.pegarKey("CHAVE PUBLICA");
            byte[] chavePrivadaBytes = Files.readAllBytes(chavePublica);
            EncodedKeySpec encoder = new X509EncodedKeySpec(chavePrivadaBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey pb = keyFactory.generatePublic(encoder);
            return pb;
        }
        catch (IOException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    public void criarSenhas(){
        try{
            KeyPairGenerator keygen = KeyPairGenerator.getInstance("RSA");
            keygen.initialize(2048);
            KeyPair keys = keygen.generateKeyPair();
            PublicKey publicKey = keys.getPublic();
            PrivateKey privateKey = keys.getPrivate();

            Path url = documento.pegarDiretorio();

            EncodedKeySpec encoder = new X509EncodedKeySpec(publicKey.getEncoded());
            Path caminhoCompleto = Paths.get(url.toString(),"chavePublica.key");
            FileOutputStream saida = new FileOutputStream(caminhoCompleto.toString());
            saida.write(encoder.getEncoded());

            encoder = new PKCS8EncodedKeySpec(privateKey.getEncoded());
            caminhoCompleto = Paths.get(url.toString(),"chavePrivate.key");
            saida = new FileOutputStream(caminhoCompleto.toString());
            saida.write(encoder.getEncoded());

            System.out.println("Senhas salvas!");
            System.out.println("Publica: " + Base64.getEncoder().encodeToString(publicKey.getEncoded()));
            System.out.println("Privada: " + Base64.getEncoder().encodeToString(privateKey.getEncoded()));

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
