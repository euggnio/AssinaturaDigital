package com.codigo;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Documento {
    public Documento() {
    }

    public Path pegarPDF() {
        JFileChooser fileChooser = windowsJFileChooser(new JFileChooser());
        FileNameExtensionFilter filtro = new FileNameExtensionFilter("Selecione um arquivo PDF!", "pdf");
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
        JFileChooser fileChooser = windowsJFileChooser(new JFileChooser());
        fileChooser.setDialogTitle(titulo);
        FileNameExtensionFilter filtro = new FileNameExtensionFilter("Selecione o arquivo .key!", "key");
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

    public Path pegarDiretorio() {
        JFileChooser fileChooser = windowsJFileChooser(new JFileChooser());
        fileChooser.setDialogTitle("SELECIONE O DIRETORIO");
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int opc = fileChooser.showOpenDialog(null);

        if (opc == JFileChooser.APPROVE_OPTION) {
            String caminhoDoArquivo = fileChooser.getSelectedFile().getAbsolutePath();
            return Paths.get(caminhoDoArquivo);
        } else if (opc == JFileChooser.CANCEL_OPTION) {
            System.out.println("Opção cancelada");
            return null;
        } else {
            System.out.println("Nenhum diretorio selecionado.");
            return null;
        }

    }



    public static JFileChooser windowsJFileChooser(JFileChooser chooser){
        LookAndFeel previousLF = UIManager.getLookAndFeel();
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            chooser = new JFileChooser();
            UIManager.setLookAndFeel(previousLF);
        } catch (IllegalAccessException | UnsupportedLookAndFeelException | InstantiationException | ClassNotFoundException e) {}
        return chooser;
    }




}
