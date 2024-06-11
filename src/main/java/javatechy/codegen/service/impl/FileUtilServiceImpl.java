package javatechy.codegen.service.impl;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javatechy.codegen.service.FileUtilService;

@Service
public class FileUtilServiceImpl implements FileUtilService {
    private static final Logger logger = Logger.getLogger(FileUtilServiceImpl.class);

    private final ClassLoader classLoader = getClass().getClassLoader();

    public void copyFile(String jsonStr) {
    }

    @Override
    public String getFileNameFromClassLoader(String fileLoc) {
        String fileName = classLoader.getResource(fileLoc)
            .getFile();
        if (fileName.startsWith("/")) {
            fileName = fileName.substring(1);
        }
        return fileName;
    }

    @Override
    public void createDirectories(String location) throws IOException {
        logger.info("[createDirectories]File Location=> " + location);
        Path path = Paths.get(location);
        logger.info("[createDirectories]File Location=> " + path.toString());
        Files.createDirectories(path);
    }

    @Override
    public String readFileData(String fileName) throws IOException {
        logger.info("Reading file => " + fileName);
        String data = new String (Files.readAllBytes(Paths.get(fileName)), StandardCharsets.UTF_8);
        logger.info("Data read  from " + fileName + " => " + data);
        return data;
    }

    @Override
    public void copyFileToDestDir(String sourceFile, String destDir) {
    }

    @Override
    public String getDataFromClassLoader(String fileLoc) throws IOException {
        return readFileData(this.getFileNameFromClassLoader(fileLoc));
    }

    public void writeDataToFile(String data, String filePath) throws IOException {
        Path path = Paths.get(filePath);
        Files.createDirectories(path.getParent());
        Files.write(path, data.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    public void appendDataToFile(String data, String filePath) throws IOException {
        Path path = Paths.get(filePath);
        Files.write(path, data.getBytes(), StandardOpenOption.APPEND);
    }

}
