package de.terrestris.utils.io

import org.apache.commons.io.IOUtils
import org.junit.Assert
import org.junit.Test
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.nio.file.Files

class ZipUtilsTest {

    @Test
    @Throws(IOException::class)
    fun testUnzip() {
        val `in` = javaClass.getResourceAsStream("test.zip")
        val zipFile = Files.createTempFile("test", "test").toFile()
        val out = FileOutputStream(zipFile)
        IOUtils.copy(`in`, out)
        `in`.close()
        out.close()
        val target = File(zipFile.parent, "child")
        ZipUtils.unzip(zipFile.toString(), target.toString())
        val result = IOUtils.toString(FileInputStream(File(target, "test/sub/content")), "UTF-8")
        Assert.assertEquals("content", result)
        val dir = File(target, "test/sub/content").toPath()
        Files.delete(dir)
        Files.delete(dir.parent)
        Files.delete(dir.parent.parent)
        Files.delete(dir.parent.parent.parent)
        Files.delete(zipFile.toPath())
    }

}
