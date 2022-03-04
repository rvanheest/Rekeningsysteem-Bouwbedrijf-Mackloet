// 
// Decompiled by Procyon v0.5.36
// 

package de.nixosoft.jlr;

import java.io.FileNotFoundException;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.File;

public class JLRGenerator
{
    private String errorMessage;
    private String outputStream;
    private File inputFile;
    private File pdfFile;
    private File auxFile;
    private File logFile;
    private boolean deleteTempTex;
    private boolean deleteAux;
    private boolean deleteLog;
    private static String newline;
    
    public JLRGenerator() {
        this.errorMessage = "No errors occurred!";
        this.outputStream = "";
        this.inputFile = null;
        this.pdfFile = null;
        this.auxFile = null;
        this.logFile = null;
        this.deleteTempTex = false;
        this.deleteAux = false;
        this.deleteLog = false;
    }
    
    public String getErrorMessage() {
        return this.errorMessage;
    }
    
    public String getOutputStream() {
        return this.outputStream;
    }
    
    public File getPDF() {
        return this.pdfFile;
    }
    
    public void deleteTempFiles(final boolean deleteTempTex, final boolean deleteAux, final boolean deleteLog) {
        this.deleteTempTex = deleteTempTex;
        this.deleteAux = deleteAux;
        this.deleteLog = deleteLog;
    }
    
    public boolean generate(final File file, final File file2, final File file3) throws IOException {
        return this.generate(new File("pdflatex"), 3, file, file2, file3);
    }
    
    public boolean generate(final int n, final File file, final File file2, final File file3) throws IOException {
        return this.generate(new File("pdflatex"), n, file, file2, file3);
    }
    
    public boolean generate(final File file, final File file2, final File file3, final File file4) throws IOException {
        return this.generate(file, 3, file2, file3, file4);
    }
    
    public boolean generate(final File file, final int n, final File inputFile, final File file2, final File directory) throws IOException {
        this.inputFile = inputFile;
        if (!this.checkingInputs(n, inputFile, file2, directory)) {
            return false;
        }
        final ProcessBuilder processBuilder = new ProcessBuilder(new String[] { file.getPath(), "--interaction=nonstopmode", "--output-directory=" + file2.getAbsolutePath(), "--aux-directory=" + inputFile.getParent(), inputFile.getAbsolutePath() });
        processBuilder.redirectErrorStream(true);
        processBuilder.directory(directory);
        for (int i = 1; i <= n; ++i) {
            final Process start = processBuilder.start();
            final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(start.getInputStream()));
            final StringBuilder sb = new StringBuilder();
            try {
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line + JLRGenerator.newline);
                }
            }
            finally {
                bufferedReader.close();
            }
            this.outputStream = sb.toString();
            try {
                final int wait = start.waitFor();
                if (!this.determiningFiles(inputFile, file2)) {
                    return false;
                }
                if (wait != 0) {
                    this.errorMessage = "Errors occurred while executing pdfLaTeX! Exit value of the process: " + wait;
                    return false;
                }
                this.errorMessage = "No errors occurred!";
            }
            catch (InterruptedException ex) {
                this.errorMessage = "The process pdfLaTeX was interrupted and an exception occurred!" + JLRGenerator.newline + ex.toString();
                return false;
            }
        }
        this.cleanUp();
        return true;
    }
    
    private boolean checkingInputs(final int n, final File file, final File file2, final File file3) throws IOException {
        if (n <= 0) {
            this.errorMessage = "The value for \"cycles\" has to be greater than 0!";
            return false;
        }
        if (!file.isFile()) {
            throw new FileNotFoundException("The file " + file.getAbsolutePath() + " does not exist or is not a file!");
        }
        if (file2 != null && !file2.isDirectory() && !file2.mkdirs()) {
            this.errorMessage = "Could not create directory: " + file2.getAbsolutePath();
            return false;
        }
        if (!file3.isDirectory()) {
            throw new IOException("The directory " + file3.getAbsolutePath() + " does not exist or is not a directory!");
        }
        return true;
    }
    
    private boolean determiningFiles(final File file, final File file2) {
        final int lastIndex = file.getName().lastIndexOf(".");
        File pdfFile;
        if (lastIndex != -1) {
            this.auxFile = new File(file.getParent() + File.separator + file.getName().substring(0, lastIndex) + ".aux");
            this.logFile = new File(file.getParent() + File.separator + file.getName().substring(0, lastIndex) + ".log");
            pdfFile = new File(file2.getAbsolutePath() + File.separator + file.getName().substring(0, lastIndex) + ".pdf");
        }
        else {
            this.auxFile = new File(file.getParent() + File.separator + file.getName() + ".aux");
            this.logFile = new File(file.getParent() + File.separator + file.getName() + ".log");
            pdfFile = new File(file2.getAbsolutePath() + File.separator + file.getName() + ".pdf");
        }
        if (pdfFile.isFile()) {
            this.pdfFile = pdfFile;
            return true;
        }
        this.errorMessage = "The pdf file could not be created or does not exist!";
        return false;
    }
    
    private void cleanUp() {
        if (this.deleteAux) {
            this.deleteFile(this.auxFile);
        }
        if (this.deleteLog) {
            this.deleteFile(this.logFile);
        }
        if (this.deleteTempTex) {
            this.deleteFile(this.inputFile);
        }
    }
    
    private void deleteFile(final File file) {
        if (file.isFile() && !file.delete()) {
            this.errorMessage = "Warning: Could not remove " + file.getAbsolutePath() + " !";
        }
    }
    
    static {
        JLRGenerator.newline = System.getProperty("line.separator");
    }
}
