package dev.ggtv.capecraft.lsp

import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.writeText
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

/**
 * Разбор uri в путь — то, от чего зависит поиск `mods` рядом с конфигом.
 *
 * Ошибка здесь не выглядит ошибкой: путь просто не находится, аддоны не
 * читаются, и их `when`-корни выглядят неизвестными. Поэтому проверяем именно
 * тот формат, который присылает редактор, а не удобный для теста.
 */
class FileUriToPathTest {

    @Test
    fun `uri из toUri разбирается, включая пробелы и кириллицу`() {
        val dir = Files.createTempDirectory("capecraft uri конфиг")
        try {
            val file = dir.resolve("capecraft.kn")
            file.writeText("limits {\n}\n")
            assertEquals(
                file.toAbsolutePath().normalize(),
                fileUriToPath(file.toUri().toString()),
            )
        } finally {
            dir.toFile().deleteRecursively()
        }
    }

    @Test
    fun `плюс в имени папки не превращается в пробел`() {
        val dir = Files.createTempDirectory("capecraft + test")
        try {
            val file = dir.resolve("capecraft.kn")
            file.writeText("limits {\n}\n")
            assertEquals(
                file.toAbsolutePath().normalize(),
                fileUriToPath(file.toUri().toString()),
            )
        } finally {
            dir.toFile().deleteRecursively()
        }
    }

    @Test
    fun `windows-uri с тремя слешами`() {
        if (File.separatorChar != '\\') return
        assertEquals(
            Path.of("C:\\Users\\KDFX Modes\\instances\\26.2\\minecraft\\config\\capecraft.kn"),
            fileUriToPath("file:///C:/Users/KDFX%20Modes/instances/26.2/minecraft/config/capecraft.kn"),
        )
    }

    @Test
    fun `uri не file - не путь`() {
        assertNull(fileUriToPath("untitled:Untitled-1"))
    }
}
