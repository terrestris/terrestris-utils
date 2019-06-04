package de.terrestris.utils.io;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Utility functions related to zip files/streams.
 */
public class ZipUtils {

    /**
     * Unzip the contents of the given zip file into the given directory.
     * @param zipFile the input zip
     * @param targetDir the output directory
     * @return the first directory contained in the archive
     * @throws IOException in case anything goes wrong
     */
    public static File unzip(String zipFile, String targetDir) throws IOException {
        return unzip(new File(zipFile), new File(targetDir));
    }

    /**
     * Unzip the contents of the given zip file into the given directory.
     * @param zipFile the input zip
     * @param targetDir the output directory
     * @return the first directory contained in the archive
     * @throws IOException in case anything goes wrong
     */
    public static File unzip(File zipFile, File targetDir) throws IOException {
        if (!targetDir.exists()) {
            targetDir.mkdir();
        }
        ZipInputStream in = new ZipInputStream(new FileInputStream(zipFile));
        ZipEntry entry = in.getNextEntry();
        File root = null;

        while (entry != null) {
            File newFile = new File(targetDir, entry.getName());
            if (entry.isDirectory()) {
                newFile.mkdirs();
                if (root == null) {
                    root = newFile;
                }
            } else {
                FileOutputStream out = new FileOutputStream(newFile);
                IOUtils.copyLarge(in, out, 0, entry.getSize());
                out.close();
            }
            in.closeEntry();
            entry = in.getNextEntry();
        }
        in.close();
        return root;
    }

}
