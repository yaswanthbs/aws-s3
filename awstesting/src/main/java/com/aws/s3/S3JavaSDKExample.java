package com.aws.s3;

import com.amazonaws.auth.BasicAWSCredentials;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;

import java.io.File;

public class S3JavaSDKExample {

	public static void main(String[] args) throws Exception {

		demoServerSideEncryptionNotResource();

	}

	public static void createAndPopulateSimpleBucket() throws Exception {

		BasicAWSCredentials awsCreds = new BasicAWSCredentials(Credentials.access_key_id,
				Credentials.secret_access_key);

		AmazonS3Client s3Client = new AmazonS3Client(awsCreds);

		BucketUtils.deleteAllBuckets(s3Client);

		String newBucketName = "mattua" + System.currentTimeMillis();
		s3Client.createBucket(newBucketName);

		final String fileName = "sometext.txt";

		File file = new File(S3JavaSDKExample.class.getResource(fileName).toURI());

		{
			PutObjectRequest putRequest1 = new PutObjectRequest(newBucketName,
					"unencrypted/" + fileName + "." + System.currentTimeMillis(), file);
			PutObjectResult response1 = s3Client.putObject(putRequest1);
			System.out.println("Uploaded object encryption status is " + response1.getSSEAlgorithm());
		}

	}

	public static void demoServerSideEncryption() throws Exception {

		BasicAWSCredentials awsCreds = new BasicAWSCredentials(Credentials.access_key_id,
				Credentials.secret_access_key);

		AmazonS3Client s3Client = new AmazonS3Client(awsCreds);

		for (Bucket bucket : s3Client.listBuckets()) {

			BucketUtils.deleteBucket(bucket.getName(), s3Client);

		}

		String newBucketName = "mattua" + System.currentTimeMillis();
		s3Client.createBucket(newBucketName);

		String policy = BucketUtils.readFileFromResources("encrypted-folder-policy.txt").replace("bucketname",
				newBucketName);

		System.out.println(policy);
		s3Client.setBucketPolicy(newBucketName, policy);

		final String fileName = "sometext.txt";

		File file = new File(S3JavaSDKExample.class.getResource(fileName).toURI());

		{
			PutObjectRequest putRequest1 = new PutObjectRequest(newBucketName,
					"unencrypted/" + fileName + "." + System.currentTimeMillis(), file);
			PutObjectResult response1 = s3Client.putObject(putRequest1);
			System.out.println("Uploaded object encryption status is " + response1.getSSEAlgorithm());
		}
		{
			PutObjectRequest putRequest1 = new PutObjectRequest(newBucketName,
					"encrypted/" + fileName + "." + System.currentTimeMillis(), file);

			try {
				PutObjectResult response1 = s3Client.putObject(putRequest1);
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("was not able to store an unencrypted file in this folder");
			}

		}
		{
			PutObjectRequest putRequest1 = new PutObjectRequest(newBucketName,
					"encrypted/" + fileName + "." + System.currentTimeMillis(), file);
			ObjectMetadata objectMetadata1 = new ObjectMetadata();
			objectMetadata1.setSSEAlgorithm(ObjectMetadata.AES_256_SERVER_SIDE_ENCRYPTION);
			putRequest1.setMetadata(objectMetadata1);

			PutObjectResult response1 = s3Client.putObject(putRequest1);
			System.out.println("Uploaded object encryption status is " + response1.getSSEAlgorithm());
		}

	}

	public static void demoServerSideEncryptionNotResource() throws Exception {

		BasicAWSCredentials awsCreds = new BasicAWSCredentials(Credentials.access_key_id,
				Credentials.secret_access_key);

		AmazonS3Client s3Client = new AmazonS3Client(awsCreds);

		BucketUtils.deleteAllBuckets(s3Client);

		String newBucketName = "sai" + System.currentTimeMillis();
		s3Client.createBucket(newBucketName);

		// Set policy  ---- still have to work on this
		/*String policy = BucketUtils.readFileFromResources("encrypted-folder-policy.txt").replace("bucketname",
				newBucketName);

		System.out.println(policy);
		s3Client.setBucketPolicy(newBucketName, policy);*/

		final String fileName = "C:\\Users\\SB00488786\\Desktop\\aws\\sometext.txt";

		/*File file = new File(S3JavaSDKExample.class.getResource(fileName).toURI());*/

		{
			PutObjectRequest putRequest1 = new PutObjectRequest(newBucketName,
					"unencrypted/" + fileName + "." + System.currentTimeMillis(), new File(fileName));
			PutObjectResult response1 = s3Client.putObject(putRequest1);
			System.out.println("Uploaded object encryption status is " + response1.getSSEAlgorithm());
		}
		{
			PutObjectRequest putRequest1 = new PutObjectRequest(newBucketName,
					"bananas/" + fileName + "." + System.currentTimeMillis(), new File(fileName));

			try {
				PutObjectResult response1 = s3Client.putObject(putRequest1);
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("was not able to store an unencrypted file in this folder");
			}

		}
		{
			PutObjectRequest putRequest1 = new PutObjectRequest(newBucketName,
					"bananas/" + fileName + "." + System.currentTimeMillis(), new File(fileName));
			ObjectMetadata objectMetadata1 = new ObjectMetadata();
			objectMetadata1.setSSEAlgorithm(ObjectMetadata.AES_256_SERVER_SIDE_ENCRYPTION);
			putRequest1.setMetadata(objectMetadata1);

			PutObjectResult response1 = s3Client.putObject(putRequest1);
			System.out.println("Uploaded object encryption status is " + response1.getSSEAlgorithm());
		}

	}

}
