// 
// Decompiled by Procyon v0.5.36
// 

package de.nixosoft.jlr;

import org.apache.velocity.context.Context;
import java.io.Writer;
import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.StringReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.io.File;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.VelocityContext;

public class JLRConverter
{
    private String errorMessage;
    private String defaultCharsetName;
    private VelocityContext contextData;
    private VelocityEngine velocityEngine;
    
    public JLRConverter(final File file) {
        this(file, "org.apache.velocity.runtime.log.NullLogChute");
    }
    
    public JLRConverter(final File file, final String value) {
        this.errorMessage = "No errors occurred!";
        this.defaultCharsetName = Charset.defaultCharset().name();
        (this.velocityEngine = new VelocityEngine()).setProperty("file.resource.loader.path", file.getAbsolutePath());
        this.velocityEngine.setProperty("runtime.log.logsystem.class", value);
        this.velocityEngine.init();
        this.contextData = new VelocityContext();
    }
    
    public void replace(final String key, final Object value) {
        this.contextData.put(key, value);
    }
    
    public void clear() {
        this.contextData = new VelocityContext();
    }
    
    public String getErrorMessage() {
        return this.errorMessage;
    }
    
    public boolean parse(final String s, final File file) throws IOException {
        try {
            return this.parse(s, file, this.defaultCharsetName);
        }
        catch (UnsupportedEncodingException ex) {
            this.errorMessage = ex.toString();
            return false;
        }
    }
    
    public boolean parse(final String s, final File file, final String charsetName) throws UnsupportedEncodingException, IOException {
        if (file.getParentFile() != null && !file.getParentFile().isDirectory() && !file.getParentFile().mkdirs()) {
            this.errorMessage = "Could not create directory: " + file.getParentFile().getAbsolutePath();
            return false;
        }
        final BufferedReader reader = new BufferedReader(new StringReader(s));
        final BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), charsetName));
        if (!this.velocityEngine.evaluate(this.contextData, writer, "myTemplate", reader)) {
            this.errorMessage = "Errors occurred and logged to velocity log";
            writer.flush();
            writer.close();
            return false;
        }
        writer.flush();
        writer.close();
        return true;
    }
    
    public boolean parse(final File file, final File file2) throws IOException {
        try {
            return this.parse(file, file2, this.defaultCharsetName);
        }
        catch (UnsupportedEncodingException ex) {
            this.errorMessage = ex.toString();
            return false;
        }
    }
    
    public boolean parse(final File file, final File file2, final String s) throws UnsupportedEncodingException, IOException {
        if (file2.getParentFile() != null && !file2.getParentFile().isDirectory() && !file2.getParentFile().mkdirs()) {
            this.errorMessage = "Could not create directory: " + file2.getParentFile().getAbsolutePath();
            return false;
        }
        final BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file2), s));
        if (!this.velocityEngine.mergeTemplate(file.getName(), s, this.contextData, writer)) {
            this.errorMessage = "Errors occurred and logged to velocity log";
            writer.flush();
            writer.close();
            return false;
        }
        writer.flush();
        writer.close();
        return true;
    }
}
