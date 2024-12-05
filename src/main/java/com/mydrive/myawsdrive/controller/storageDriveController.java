package com.mydrive.myawsdrive.controller;


import com.mydrive.myawsdrive.service.storageDriveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/drive")
public class storageDriveController {

    @Autowired
    private storageDriveService driveService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam(value = "file")  MultipartFile file){
       return new ResponseEntity<>(driveService.uploadFile(file),HttpStatus.OK);
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<?> downloadFile(@PathVariable String fileName){
       byte[] downloadedData = driveService.downloadFile(fileName);
        ByteArrayResource data = new ByteArrayResource(downloadedData);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(downloadedData.length)
                .header("Content-disposition", "attachment; filename = \"" + fileName + "\"")
                .body(data);

    }
    @DeleteMapping("/remove/{fileName}")
    public ResponseEntity<?> removeFile(@PathVariable String fileName){
        return new ResponseEntity<>(driveService.deleteFile(fileName),HttpStatus.OK);
    }
}
