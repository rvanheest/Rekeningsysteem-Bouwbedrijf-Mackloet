// 
// Decompiled by Procyon v0.5.36
// 

package de.nixosoft.jlr;

import java.awt.Component;
import javax.swing.JOptionPane;
import java.awt.Desktop;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.File;

public final class JLROpener
{
    private static String newline;
    
    private JLROpener() {
    }
    
    public static void open(final File file, final File file2) throws IOException {
        if (!file.isFile()) {
            throw new FileNotFoundException("The file " + file.getAbsolutePath() + " does not exist or is not a file!");
        }
        if (!file2.isFile()) {
            throw new FileNotFoundException("The file " + file2.getAbsolutePath() + " does not exist or is not a file!");
        }
        new ProcessBuilder(new String[] { file.getAbsolutePath(), file2.getAbsolutePath() }).start();
    }
    
    public static void open(final File file) throws IOException {
        if (!file.isFile()) {
            throw new FileNotFoundException("The file " + file.getAbsolutePath() + " does not exist or is not a file!");
        }
        if (Desktop.isDesktopSupported()) {
            if (Desktop.getDesktop().isSupported(Desktop.Action.OPEN)) {
                try {
                    Desktop.getDesktop().open(file);
                }
                catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Could not open " + file.getAbsolutePath() + JLROpener.newline + "The specified file has no associated application or the associated application fails to be launched!", "FileOpenError", 0);
                }
            }
            else {
                JOptionPane.showMessageDialog(null, "Could not open " + file.getAbsolutePath() + JLROpener.newline + "Desktop.Action.OPEN is not supported!", "FileOpenError", 0);
            }
        }
        else {
            JOptionPane.showMessageDialog(null, "Could not open " + file.getAbsolutePath() + JLROpener.newline + "Desktop is not supported on the current platform!", "FileOpenError", 0);
        }
    }
    
    public static void print(final File file) throws IOException {
        if (!file.isFile()) {
            throw new FileNotFoundException("The file " + file.getAbsolutePath() + " does not exist or is not a file!");
        }
        if (Desktop.isDesktopSupported()) {
            if (Desktop.getDesktop().isSupported(Desktop.Action.PRINT)) {
                try {
                    Desktop.getDesktop().print(file);
                }
                catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Could not print " + file.getAbsolutePath() + JLROpener.newline + "The specified file has no associated application or the associated application fails to be launched!", "FileOpenError", 0);
                }
            }
            else {
                JOptionPane.showMessageDialog(null, "Could not print " + file.getAbsolutePath() + JLROpener.newline + "Desktop.Action.PRINT is not supported!", "FilePrintError", 0);
            }
        }
        else {
            JOptionPane.showMessageDialog(null, "Could not print " + file.getAbsolutePath() + JLROpener.newline + "Desktop is not supported on the current platform!", "FilePrintError", 0);
        }
    }
    
    static {
        JLROpener.newline = System.getProperty("line.separator");
    }
}
