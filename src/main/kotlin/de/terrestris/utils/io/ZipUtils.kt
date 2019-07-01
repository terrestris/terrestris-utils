package de.terrestris.utils.io

import org.apache.commons.io.IOUtils
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.file.Files
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

/**
 * Utility functions related to zip files/streams.
 */
object ZipUtils {

    /**
     * Unzip the contents of the given zip file into the given directory.
     * @param zipFile the input zip
     * @param targetDir the output directory
     * @return the first directory contained in the archive
     */
    fun unzip(zipFile: String, targetDir: String): File? {
        return unzip(File(zipFile), File(targetDir))
    }

    /**
     * Unzip the contents of the given zip file into the given directory.
     * @param zipFile the input zip
     * @param targetDir the output directory
     * @return the first directory contained in the archive
     */
    fun unzip(zipFile: File, targetDir: File): File? {
        if (!targetDir.exists()) {
            Files.createDirectory(targetDir.toPath())
        }
        val `in` = ZipInputStream(FileInputStream(zipFile))
        var entry: ZipEntry? = `in`.nextEntry
        var root: File? = null

        while (entry != null) {
            val newFile = File(targetDir, entry.name)
            if (entry.isDirectory) {
                Files.createDirectories(newFile.toPath())
                if (root == null) {
                    root = newFile
                }
            } else {
                val out = FileOutputStream(newFile)
                IOUtils.copyLarge(`in`, out, 0, entry.size)
                out.close()
            }
            `in`.closeEntry()
            entry = `in`.nextEntry
        }
        `in`.close()
        return root
    }

}// prevent instantiation
