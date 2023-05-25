package com.codigo;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Documento {
    public Documento() {
    }

    public Path pegarPDF() {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filtro = new FileNameExtensionFilter("Apenas PDF!", "pdf");
        fileChooser.setFileFilter(filtro);

        int opc = fileChooser.showOpenDialog(null);
        if (opc == JFileChooser.APPROVE_OPTION) {
            String caminhoDoArquivo = fileChooser.getSelectedFile().getAbsolutePath();
            return Paths.get(caminhoDoArquivo);
        } else {
            System.out.println("Nenhum arquivo selecionado.");
            return null;
        }
    }

    public Path pegarKey(String titulo) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle(titulo);
        FileNameExtensionFilter filtro = new FileNameExtensionFilter("Apenas key!", "key");
        fileChooser.setFileFilter(filtro);

        int opc = fileChooser.showOpenDialog(null);
        if (opc == JFileChooser.APPROVE_OPTION) {
            String caminhoDoArquivo = fileChooser.getSelectedFile().getAbsolutePath();
            return Paths.get(caminhoDoArquivo);
        } else {
            System.out.println("Nenhum arquivo selecionado.");
            return null;
        }
    }

    public Path pegarDiretorio(){
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("SELECIONE O DIRETORIO");
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int opc = fileChooser.showOpenDialog(null);

        if (opc == JFileChooser.APPROVE_OPTION) {
            String caminhoDoArquivo = fileChooser.getSelectedFile().getAbsolutePath();
            return Paths.get(caminhoDoArquivo);
        } else {
            System.out.println("Nenhum diretorio selecionado.");
            return null;
        }

    }




}
