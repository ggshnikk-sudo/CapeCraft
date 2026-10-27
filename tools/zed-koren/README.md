# Zed + capecraft-lsp

Расширение Zed для `.kn`/`.crn` (язык `KoreN`) и LSP-сервера `capecraft`.

## Windows: установка

1. Скачать `capecraft-lsp.jar` из
   [релиза `lsp-v*`](https://github.com/ggshnikk-sudo/CapeCraft/releases) и
   положить, например, в `%LOCALAPPDATA%\capecraft\capecraft-lsp.jar`.
2. Собрать/взять готовый `koren_zed.wasm` (`cargo build --release --target wasm32-wasip2`)
   и разложить расширение в `%LOCALAPPDATA%\Zed\extensions\installed\koren`:
   `extension.wasm`, `extension.toml`, `languages/KoreN/config.toml`.
   Запись `koren` добавить в `%LOCALAPPDATA%\Zed\extensions\index.json`
   (см. `install.sh` — там же формат index).
3. В `%APPDATA%\Zed\settings.json` явно указать бинарник сервера: путь из
   расширения по умолчанию linux-only, поэтому на Windows нужен override.

```json
{
  "languages": {
    "KoreN": {
      "semantic_tokens": "full",
      "language_servers": ["capecraft"]
    }
  },
  "lsp": {
    "capecraft": {
      "binary": {
        "path": "C:\\Program Files\\Microsoft\\jdk-21.0.12.101-hotspot\\bin\\java.exe",
        "arguments": [
          "-jar",
          "C:\\Users\\<user>\\AppData\\Local\\capecraft\\capecraft-lsp.jar"
        ]
      }
    }
  }
}
```

4. Перезапустить Zed, открыть `.kn`-файл. В
   `%LOCALAPPDATA%\Zed\logs\Zed.log` должна быть строка
   `starting language server process` с путями из `lsp.capecraft.binary` и не
   должно быть `failed to spawn`.

## Linux/macOS

`install.sh` собирает wasm и раскладывает расширение; бинарник и jar берутся из
`CAPECRAFT_JAVA` / `CAPECRAFT_LSP_JAR`, если переменные заданы (значения по
умолчанию в `src/lib.rs` — linux-only).

## Проверка сервера вручную

```powershell
java -jar capecraft-lsp.jar
```

и отправить `initialize` по stdio: сервер отвечает
`serverInfo = { name: "capecraft-lsp", version: <версия> }`.
