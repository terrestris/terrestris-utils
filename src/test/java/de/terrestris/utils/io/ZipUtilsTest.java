package de.terrestris.utils.io;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class ZipUtilsTest {

    @Test
    public void testUnzip() throws IOException {
        InputStream in = getClass().getResourceAsStream("test.zip");
        File zipFile = Files.createTempFile("test", "test").toFile();
        OutputStream out = new FileOutputStream(zipFile);
        IOUtils.copy(in, out);
        in.close();
        out.close();
        File target = new File(zipFile.getParent(), "child");
        ZipUtils.unzip(zipFile.toString(), target.toString());
        String result = IOUtils.toString(new FileInputStream(new File(target, "test/sub/content")), StandardCharsets.UTF_8);
        Assert.assertEquals("content", result);
        Path dir = new File(target, "test/sub/content").toPath();
        Files.delete(dir);
        Files.delete(dir.getParent());
        Files.delete(dir.getParent().getParent());
        Files.delete(dir.getParent().getParent().getParent());
        Files.delete(zipFile.toPath());
    }

    @Test
    public void testUnzipReplace() throws IOException {
        InputStream in = getClass().getResourceAsStream("test.zip");
        File zipFile = Files.createTempFile("test", "test").toFile();
        OutputStream out = new FileOutputStream(zipFile);
        IOUtils.copy(in, out);
        in.close();
        out.close();
        File target = new File(zipFile.getParent(), "child");
        ZipUtils.unzip(zipFile.toString(), target.toString(), true);
        String result = IOUtils.toString(new FileInputStream(new File(target, "test/sub/content")), StandardCharsets.UTF_8);
        Assert.assertEquals("content", result);
        Path dir = new File(target, "test/sub/content").toPath();
        Files.delete(dir);
        Files.delete(dir.getParent());
        Files.delete(dir.getParent().getParent());
        Files.delete(dir.getParent().getParent().getParent());
        Files.delete(zipFile.toPath());
    }

}
