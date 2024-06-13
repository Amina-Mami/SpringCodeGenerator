package javatechy.codegen.service;

import java.io.IOException;

public interface FileUtilService {

    String getFileNameFromClassLoader(String fileLoc);
    
    String getDataFromClassLoader(String fileLoc) throws IOException;

    void createDirectories(String location) throws IOException;

    String readFileData(String fileName) throws IOException;

    void copyFileToDestDir(String sourceFile, String destDir);

    void writeDataToFile(String data, String fileName) throws IOException;

    void appendDataToFile(String relationshipContent, String filePath) throws IOException;

    String readDataFromFile(String fileName) throws IOException;
}
