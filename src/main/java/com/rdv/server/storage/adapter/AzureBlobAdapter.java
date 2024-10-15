package com.rdv.server.storage.adapter;

import com.rdv.server.storage.to.ContainerType;
import com.rdv.server.util.RandomSequenceGenerator;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Base64;

@Component
public class AzureBlobAdapter {

    private static final Logger LOG = LoggerFactory.getLogger(AzureBlobAdapter.class);
    private static final String USER_PROFILE_PHOTOS_URL = "https://rdv-filestorage.blob.core.windows.net/userphotos/";
    private static final String EVENT_POSTERS_URL = "https://rdv-filestorage.blob.core.windows.net/eventposters/";

    @Autowired
    private CloudBlobClient cloudBlobClient;
    @Autowired
    private RandomSequenceGenerator randomSequenceGenerator;


    public URI uploadFile(MultipartFile multipartFile, ContainerType containerType, Long userId){
        URI uri = null;
        CloudBlockBlob blob;

        String blobName = userId != null ? userId + "__" + randomSequenceGenerator.generateAlphaNumericSequence() + "_" + multipartFile.getOriginalFilename() : randomSequenceGenerator.generateAlphaNumericSequence() + "_" + multipartFile.getOriginalFilename();

        try {
            CloudBlobContainer container = cloudBlobClient.getContainerReference(containerType.getValue());
            blob = container.getBlockBlobReference(blobName);
            blob.upload(multipartFile.getInputStream(), -1);
            uri = blob.getUri();
        } catch (URISyntaxException | StorageException | IOException e) {
            LOG.info("An error has occurred while uploading the file ", e);
        }

        return uri;
    }

    public String getBlob(String blobName, ContainerType containerType) {
        CloudBlockBlob blobRetrieved;
        byte[] bytes = null;

        try {
            CloudBlobContainer container = cloudBlobClient.getContainerReference(containerType.getValue());
            blobRetrieved = container.getBlockBlobReference(blobName);
            if(blobRetrieved.exists()) {
                bytes = blobRetrieved.openInputStream().readAllBytes();
            }
        } catch (URISyntaxException | StorageException | IOException e) {
            LOG.info("An error has occurred while retrieving the file ", e);
        }

        return bytes != null ? Base64.getEncoder().encodeToString(bytes) : null;
    }

    public void deleteBlob(String blobName, ContainerType containerType) {
        try {
            CloudBlobContainer container = cloudBlobClient.getContainerReference(containerType.getValue());
            CloudBlockBlob blobToBeDeleted = container.getBlockBlobReference(blobName);
            blobToBeDeleted.deleteIfExists();
        } catch (URISyntaxException | StorageException e) {
            LOG.info("An error has occurred while deleting the file ", e);
        }
    }

    public void deleteProfilePicture(String profilePictureUrl){
        if(profilePictureUrl != null && !profilePictureUrl.isEmpty()) {
            String blobName = profilePictureUrl.replace(USER_PROFILE_PHOTOS_URL, StringUtils.EMPTY);
            deleteBlob(blobName, ContainerType.USER_PHOTOS);
        }
    }

}
