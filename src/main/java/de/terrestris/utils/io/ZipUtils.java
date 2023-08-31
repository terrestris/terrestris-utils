package de.terrestris.utils.io;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static org.apache.logging.log4j.LogManager.getLogger;

/**
 * Utility functions related to zip files/streams.
 */
public class ZipUtils {

  private static final Logger LOG = getLogger(ZipUtils.class);

  private ZipUtils() {
    // prevent instantiation
  }

  /**
   * Unzip the contents of the given zip file into the given directory.
   *
   * @param zipFile   the input zip
   * @param targetDir the output directory
   * @return the first directory contained in the archive
   * @throws IOException in case anything goes wrong
   */
  public static File unzip(String zipFile, String targetDir) throws IOException {
    return unzip(new File(zipFile), new File(targetDir));
  }

  /**
   * Unzip the contents of the given zip file into the given directory.
   *
   * @param zipFile   the input zip
   * @param targetDir the output directory
   * @return the first directory contained in the archive
   * @throws IOException in case anything goes wrong
   */
  public static File unzip(File zipFile, File targetDir) throws IOException {
    return unzip(zipFile, targetDir, false);
  }

  /**
   * Unzip the contents of the given zip file into the given directory. If replace is set to true, the directory will be
   * cleaned if existing and not empty. If anything goes wrong while cleaning, the error is ignored and the unzip will
   * still commence.
   *
   * @param zipFile   the input zip
   * @param targetDir the output directory
   * @param replace   if true, the contents of the directory will be deleted before unzip, if it exists
   * @return the first directory contained in the archive
   * @throws IOException in case anything goes wrong
   */
  public static File unzip(File zipFile, File targetDir, boolean replace) throws IOException {
    return unzip(zipFile, targetDir, replace, Charset.defaultCharset());
  }

    /**
     * Unzip the contents of the given zip file into the given directory. If replace is set to true, the directory will be
     * cleaned if existing and not empty. If anything goes wrong while cleaning, the error is ignored and the unzip will
     * still commence.
     *
     * @param zipFile   the input zip
     * @param targetDir the output directory
     * @param replace   if true, the contents of the directory will be deleted before unzip, if it exists
     * @param charset   the file name charset of the zip directory
     * @return the first directory contained in the archive
     * @throws IOException in case anything goes wrong
     */
  public static File unzip(File zipFile, File targetDir, boolean replace, Charset charset) throws IOException {
    if (!targetDir.exists()) {
      Files.createDirectory(targetDir.toPath());
    }
    try (ZipInputStream in = new ZipInputStream(new FileInputStream(zipFile), charset)) {
      ZipEntry entry = in.getNextEntry();
      File root = null;

      while (entry != null) {
        File newFile = new File(targetDir, entry.getName());
        if (entry.isDirectory()) {
          root = handleDirectory(replace, newFile, root);
        } else {
          FileOutputStream out = new FileOutputStream(newFile);
          IOUtils.copyLarge(in, out, 0, entry.getSize());
          out.close();
        }
        in.closeEntry();
        entry = in.getNextEntry();
      }
      return root;
    }
  }

  private static File handleDirectory(boolean replace, File newFile, File root) throws IOException {
    Files.createDirectories(newFile.toPath());
    if (root == null) {
      root = newFile;
      if (replace) {
        try {
          FileUtils.cleanDirectory(root);
        } catch (Exception e) {
          // ignore if e.g. locked files cannot be deleted
          LOG.debug("Couldn't clean target directory: {}", e.getMessage());
          LOG.trace("Stack trace:", e);
        }
      }
    }
    return root;
  }

}
