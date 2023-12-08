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
  public void testZip() throws IOException {
    File zipFile = copyTestFile();
    File target = new File(zipFile.getParent(), "child");
    ZipUtils.unzip(zipFile.toString(), target.toString());
    Path tmp2 = Files.createTempFile("test", "test");
    ZipUtils.zip(tmp2.toFile(), new File("/tmp/child/test"), false);
    Path tmp3 = Files.createTempFile("test", "test");
    Files.delete(tmp3);
    Files.createDirectory(tmp3);
    ZipUtils.unzip(tmp2.toFile(), tmp3.toFile());
    String content = IOUtils.toString(Files.newInputStream(new File(tmp3.toFile(), "test/sub/content").toPath()), StandardCharsets.UTF_8);
    Assert.assertEquals("content", content);
    Files.delete(new File(tmp3.toFile(), "test/sub/content").toPath());
    Files.delete(new File(tmp3.toFile(), "test/sub").toPath());
    Files.delete(new File(tmp3.toFile(), "test").toPath());
    Files.delete(tmp3);
    Files.delete(tmp2);
    Files.delete(zipFile.toPath());
    Path dir = new File(target, "test/sub/content").toPath();
    Files.delete(dir);
    Files.delete(dir.getParent());
    Files.delete(dir.getParent().getParent());
    Files.delete(dir.getParent().getParent().getParent());
  }

  private File copyTestFile() throws IOException {
    InputStream in = ZipUtilsTest.class.getResourceAsStream("test.zip");
    File zipFile = Files.createTempFile("test", "test").toFile();
    OutputStream out = new FileOutputStream(zipFile);
    IOUtils.copy(in, out);
    in.close();
    out.close();
    return zipFile;
  }

  @Test
  public void testUnzip() throws IOException {
    File zipFile = copyTestFile();
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
    File zipFile = copyTestFile();
    File target = new File(zipFile.getParent(), "child");
    ZipUtils.unzip(zipFile, target, true);
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
