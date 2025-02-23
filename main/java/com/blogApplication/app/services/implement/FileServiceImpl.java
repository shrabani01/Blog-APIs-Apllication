package com.blogApplication.app.services.implement;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.blogApplication.app.services.FileService;

@Service
public class FileServiceImpl implements FileService {

    private static final Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    @Override
    public String uploadImage(String path, MultipartFile file) throws IOException {
        // File name
        String originalFileName = file.getOriginalFilename();
        logger.info("Original file name: {}", originalFileName);

        // Generate random string for the new file name
        String randomId = UUID.randomUUID().toString();
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String generatedFileName = randomId.concat(fileExtension);

        // File path
        String filePath = path + File.separator + generatedFileName;

        // Create folder if it doesn't exist
        File directory = new File(path);
        if (!directory.exists()) {
            if (directory.mkdirs()) {
                logger.info("Directory created at: {}", path);
            } else {
                throw new IOException("Failed to create directory: " + path);
            }
        }

        // Copy file
        try {
            Files.copy(file.getInputStream(), Paths.get(filePath));
            logger.info("File uploaded successfully to: {}", filePath);
        } catch (IOException e) {
            logger.error("Error while uploading file: {}", e.getMessage());
            throw new IOException("File upload failed: " + e.getMessage());
        }

        return generatedFileName; // Return the unique file name
    }

    @Override
    public InputStream getResource(String path, String fileName) throws FileNotFoundException {
        String fullPath = path + File.separator + fileName;
        logger.info("Fetching file from path: {}", fullPath);

        try {
            return new FileInputStream(fullPath);
        } catch (FileNotFoundException e) {
            logger.error("File not found: {}", fullPath);
            throw e;
        }
    }
}
