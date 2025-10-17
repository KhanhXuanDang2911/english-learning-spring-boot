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
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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
        log.info("Uploading file to Azure: fileName={}, containerName={}, contentType={}", fileName, containerName,
                file.getContentType());
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

    @Override
    public String uploadBytes(byte[] data, String filename, String contentType) {
        String fileName = UUID.randomUUID() + "-" + filename;
        log.info("Uploading bytes to Azure: fileName={}, containerName={}, contentType={}", fileName, containerName,
                contentType);
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(data);

            BlockBlobClient blobClient = new BlobClientBuilder()
                    .connectionString(connectionString)
                    .containerName(containerName)
                    .blobName(fileName)
                    .buildClient()
                    .getBlockBlobClient();

            blobClient.upload(inputStream, data.length, true);

            BlobHttpHeaders headers = new BlobHttpHeaders()
                    .setContentType(contentType);
            blobClient.setHttpHeaders(headers);

            log.info("Upload successful: {}", blobClient.getBlobUrl());
            return blobClient.getBlobUrl();

        } catch (Exception e) {
            log.error("Upload failed due to unexpected error: {}", e.getMessage(), e);
            throw new AppException(ErrorCode.UPLOAD_FILE_FAILED);
        }
    }

    @Override
    public String uploadVideo(MultipartFile file) {
        String fileName = UUID.randomUUID() + "-" + file.getOriginalFilename();
        log.info("Uploading (multi-thread) to Azure: {}", fileName);

        BlockBlobClient blobClient = new BlobClientBuilder()
                .connectionString(connectionString)
                .containerName(containerName)
                .blobName(fileName)
                .buildClient()
                .getBlockBlobClient();

        final int CHUNK_SIZE = 8 * 1024 * 1024; 
        int numThreads = 8;
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);

        try (var inputStream = file.getInputStream()) {
            byte[] buffer = new byte[CHUNK_SIZE];
            int bytesRead;
            int blockNum = 0;
            List<String> blockIds = new ArrayList<>();
            List<Future<?>> futures = new ArrayList<>();

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byte[] chunkData = Arrays.copyOf(buffer, bytesRead);
                String blockId = Base64.getEncoder()
                        .encodeToString(String.format("%06d", blockNum++).getBytes());
                blockIds.add(blockId);

                futures.add(executor.submit(() -> {
                    try (var chunkStream = new ByteArrayInputStream(chunkData)) {
                        blobClient.stageBlock(blockId, chunkStream, chunkData.length);
                        log.debug("Uploaded block {}", blockId);
                    } catch (Exception e) {
                        log.error("Block upload failed: {}", blockId, e);
                        throw new RuntimeException(e);
                    }
                }));
            }

            for (Future<?> f : futures) {
                f.get();
            }

            blobClient.commitBlockList(blockIds);

            BlobHttpHeaders headers = new BlobHttpHeaders()
                    .setContentType(file.getContentType() != null ? file.getContentType() : "video/mp4")
                    .setCacheControl("public, max-age=31536000")
                    .setContentDisposition("inline; filename=\"" + fileName + "\"");

            blobClient.setHttpHeaders(headers);

            executor.shutdown();

            String url = blobClient.getBlobUrl();
            log.info("Upload successful: {}", url);
            return url;

        } catch (Exception e) {
            executor.shutdownNow();
            log.error("Upload failed: {}", e.getMessage(), e);
            throw new AppException(ErrorCode.UPLOAD_FILE_FAILED);
        }
    }


}
