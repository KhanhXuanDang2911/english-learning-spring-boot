package elearningspringboot.service.impl;

import com.azure.storage.blob.BlobClientBuilder;
import com.azure.storage.blob.models.BlobHttpHeaders;
import com.azure.storage.blob.specialized.BlockBlobClient;
import elearningspringboot.enumeration.ErrorCode;
import elearningspringboot.exception.AppException;
import elearningspringboot.service.AzureBlobService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@Service
public class AzureBlobServiceImpl implements AzureBlobService {

    @Value("${azure.storage.connection-string}")
    private String connectionString;

    @Value("${azure.storage.container-name}")
    private String containerName;

    @Override
    public String uploadFile(MultipartFile file) {
        String fileName = UUID.randomUUID() + "-" + file.getOriginalFilename();
        log.info("Uploading file to Azure: fileName={}, containerName={}, contentType={}", fileName, containerName, file.getContentType());
        try {
            byte[] bytes = file.getBytes();
            ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);

            BlockBlobClient blobClient = new BlobClientBuilder()
                    .connectionString(connectionString)
                    .containerName(containerName)
                    .blobName(fileName)
                    .buildClient()
                    .getBlockBlobClient();

            blobClient.upload(inputStream, bytes.length, true);

            BlobHttpHeaders headers = new BlobHttpHeaders()
                    .setContentType(file.getContentType());
            blobClient.setHttpHeaders(headers);

            log.info("Upload successful: {}", blobClient.getBlobUrl());
            return blobClient.getBlobUrl();

        } catch (IOException e) {
            log.error("Upload failed due to IOException: {}", e.getMessage(), e);
            throw new AppException(ErrorCode.UPLOAD_FILE_FAILED);
        } catch (Exception e) {
            log.error("Upload failed due to unexpected error: {}", e.getMessage(), e);
            throw new AppException(ErrorCode.UPLOAD_FILE_FAILED);
        }
    }


    @Override
    public boolean deleteFile(String fileName) {
        BlockBlobClient blobClient = new BlobClientBuilder()
                .connectionString(connectionString)
                .containerName(containerName)
                .blobName(fileName)
                .buildClient()
                .getBlockBlobClient();

        if (blobClient.exists()) {
            blobClient.delete();
            return true;
        }

        return false;
    }
}
