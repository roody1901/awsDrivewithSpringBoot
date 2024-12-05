package com.mydrive.myawsdrive.service;


import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class storageDriveService {

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Autowired
    private AmazonS3 s3Client;


    Logger logger = LoggerFactory.getLogger(Logger.class);

    public String uploadFile(MultipartFile file){

            String fileName = System.currentTimeMillis() + "_"+ file.getOriginalFilename();
            File convertedFile = MultiPartToFile(file);
            s3Client.putObject(new PutObjectRequest(bucketName,fileName,convertedFile));

            convertedFile.delete(); //so that this file will not be available in file;

        return "file: "+ fileName + "  uploaded to "+ bucketName;
    }

    public byte[] downloadFile(String fileName){
       S3Object s3Object =  s3Client.getObject(new GetObjectRequest(bucketName,fileName));
        S3ObjectInputStream objectInputStream = s3Object.getObjectContent();
        try {
           byte[] content =  IOUtils.toByteArray(objectInputStream);
           return content;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public String deleteFile(String fileName){
        s3Client.deleteObject(bucketName,fileName);
        return "file deleted "+ fileName;
    }

    private File MultiPartToFile(MultipartFile file)  {
        File convertFile = new File(file.getOriginalFilename());
        try(FileOutputStream fos = new FileOutputStream(convertFile)){
                fos.write(file.getBytes());
        }catch (IOException e){
            logger.error("error at + e");
        }
        return convertFile;
    }
}
