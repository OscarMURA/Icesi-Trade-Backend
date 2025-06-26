package com.trade.icesi_trade.Service.blob;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.specialized.BlockBlobClient;
import com.azure.storage.blob.models.BlobHttpHeaders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Service
public class AzureBlobService {

    private final BlobContainerClient container; // inyectado por Spring Cloud Azure

    // Como ya definiste spring.cloud.azure.storage.blob.container-name=media
    // Spring crea automáticamente este bean
    public AzureBlobService(BlobContainerClient container) {
        this.container = container;
        if (!container.exists()) { // crea el contenedor en Azurite/Azure si falta
            container.create();
        }
    }

    public String uploadImage(MultipartFile file) throws IOException {
        String blobName = "products/" + UUID.randomUUID() + "-" + file.getOriginalFilename();

        BlobClient blob = container.getBlobClient(blobName);
        BlockBlobClient blockBlob = blob.getBlockBlobClient();

        // Convertir el MultipartFile a bytes y luego a ByteArrayInputStream
        // Esto asegura que el stream soporte mark/reset
        byte[] fileBytes = file.getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(fileBytes);

        // Subida con overwrite=true
        blockBlob.upload(inputStream, fileBytes.length, true);

        BlobHttpHeaders headers = new BlobHttpHeaders()
                .setContentType(file.getContentType());
        blockBlob.setHttpHeaders(headers);

        return blob.getBlobUrl();
    }

    public boolean deleteImageByUrl(String imageUrl) {
        if (imageUrl == null || imageUrl.isEmpty()) {
            System.err.println("AzureBlobService: URL de imagen es null o vacía");
            return false;
        }

        try {
            // Extraer el nombre del blob de la URL
            String[] parts = imageUrl.split("/media/");
            if (parts.length < 2) {
                System.err.println("AzureBlobService: No se pudo extraer el nombre del blob de la URL: " + imageUrl);
                return false;
            }

            // Decodificar el nombre del blob para manejar caracteres especiales como %2F
            String blobName = URLDecoder.decode(parts[1], StandardCharsets.UTF_8.toString());
            System.out.println("AzureBlobService: Intentando eliminar blob: " + blobName);

            BlobClient blob = container.getBlobClient(blobName);
            if (blob.exists()) {
                blob.delete();
                System.out.println("AzureBlobService: Blob eliminado exitosamente: " + blobName);
                return true;
            } else {
                System.err.println("AzureBlobService: El blob no existe: " + blobName);
                return false;
            }
        } catch (Exception e) {
            System.err.println("AzureBlobService: Error al eliminar imagen: " + imageUrl);
            System.err.println("AzureBlobService: Error: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
}