package ru.stephen.filmlibrary.library.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

import static ru.stephen.filmlibrary.library.constants.FileDirectoriesConstants.FILMS_UPLOAD_DIRECTORY;

@Slf4j
public class FileHelper {
    private FileHelper () {}

    public static String createFile (final MultipartFile file) {
        String filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String resultFilename = "";
        //files/films/file_name
        try {
            Path path = Paths.get(FILMS_UPLOAD_DIRECTORY + "/" + filename).toAbsolutePath().normalize();
            if (!path.toFile().exists()) {
                Files.createDirectories(path);
            }
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            resultFilename = path.toString();
        } catch (IOException e) {
            log.error("FileHelper#createFile(): {}", e.getMessage());
        }
        return resultFilename;
    }
}
