package com.rdv.server.storage.controller;


import com.rdv.server.storage.adapter.AzureBlobAdapter;
import com.rdv.server.storage.to.ContainerType;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;

/**
 * @author davidgarcia
 */

@RestController
@CrossOrigin
@RequestMapping("/images")
@Tag(name = "FileStorageController", description = "Set of endpoints to handle the azure file storage logic")
public class FileStorageController {

    @Autowired
    private AzureBlobAdapter azureBlobAdapter;


    @PostMapping("/upload")
    public ResponseEntity upload(@Parameter(description = "The image to upload") @RequestParam MultipartFile multipartFile,
                           @Parameter(description = "The container type (Possible values: USER_PHOTOS, EVENT_POSTERS)", example="USER_PHOTOS") @RequestParam String containerType,
                           @Parameter(description = "The user id (if available)") @RequestParam(required=false) Long userId){
        URI url = azureBlobAdapter.uploadFile(multipartFile, ContainerType.valueOf(containerType), userId);
        return ResponseEntity.ok(url);
    }

    @DeleteMapping("/delete")
    public ResponseEntity delete(@RequestParam String blobName, @RequestParam String containerType){
        azureBlobAdapter.deleteBlob(blobName, ContainerType.valueOf(containerType));
        return ResponseEntity.ok().build();
    }

}