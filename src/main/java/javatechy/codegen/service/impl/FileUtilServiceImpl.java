package javatechy.codegen.service.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
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

        Path filePath = Paths.get(fileName);
        if (!Files.exists(filePath)) {
            logger.error("File not found: " + fileName);
            throw new FileNotFoundException("File not found: " + fileName);
        }

        String data = new String(Files.readAllBytes(filePath), StandardCharsets.UTF_8);
        logger.info("Data read from " + fileName + " => " + data);

        return data;
    }




    @Override
    public void copyFileToDestDir(String sourceFile, String destDir) {
    }

    @Override
    public String getDataFromClassLoader(String resourcePath) throws IOException {
        URL resource = getClass().getClassLoader().getResource(resourcePath);
        if (resource == null) {
            throw new IOException("Resource not found: " + resourcePath);
        }
        try {
            URI uri = resource.toURI();
            Path path = Paths.get(uri);
            return new String(Files.readAllBytes(path));
        } catch (URISyntaxException e) {
            throw new IOException("Invalid URI syntax for resource: " + resourcePath, e);
        }
    }



    public void appendDataToFile(String data, String filePath) throws IOException {
        Path path = Paths.get(filePath);
        Files.write(path, data.getBytes(), StandardOpenOption.APPEND);
    }

    @Override
    public String readDataFromFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        if (!Files.exists(path)) {
            throw new IOException("File not found: " + filePath);
        }
        return new String(Files.readAllBytes(path));
    }

    @Override
    public void writeDataToFile(String data, String filePath) throws IOException {
        Path path = Paths.get(filePath);
        Files.createDirectories(path.getParent());
        Files.write(path, data.getBytes());
    }


}
