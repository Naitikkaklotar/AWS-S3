package com.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.PutObjectRequest;

@Service
public class StorageService {

	@Value("${application.bucket.name}")
	private String bucketName;

	public String uploadFile(MultipartFile file) {

		AWSCredentials credentials = new BasicAWSCredentials("AKIA2UC264FRYQ3VKOWT",
				"qJ0MRGLR2kHSRDLKrvtgkooJwOAWjflM01IWwu+K");

		AmazonS3 s3client = AmazonS3ClientBuilder.standard()
				.withCredentials(new AWSStaticCredentialsProvider(credentials)).withRegion(Regions.US_EAST_1).build();

		File fileObj = convertMultiPartFileToFile(file);
		String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
		s3client.putObject(new PutObjectRequest(bucketName, fileName, fileObj));
		fileObj.delete();
		return "File uploaded : " + fileName;
	}

	public byte[] downloadFile(String fileName) {
		// S3Object s3Object = s3Client.getObject(bucketName, fileName);
		// S3ObjectInputStream inputStream = s3Object.getObjectContent();
		// try {
		// byte[] content = IOUtils.toByteArray(inputStream);
		// return content;
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		return null;
	}

	public String deleteFile(String fileName) {
		// s3Client.deleteObject(bucketName, fileName);
		return fileName + " removed ...";
	}

	private File convertMultiPartFileToFile(MultipartFile file) {
		File convertedFile = new File(file.getOriginalFilename());
		try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
			fos.write(file.getBytes());
		} catch (IOException e) {

		}
		return convertedFile;
	}
}