package server;

import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.staticFiles;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import javax.servlet.http.Part;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.PutObjectRequest;

import spark.Request;

public class Main {
	public static void main(String[] args) {

		File uploadDir = new File("upload");
		uploadDir.mkdir(); // create the upload directory if it doesn't exist

		staticFiles.externalLocation("upload");

		get("/hello", (req, res) -> "Hello World");

		post("/upload", (request, response) -> {
			Path tempFile = Files.createTempFile(uploadDir.toPath(), "", "");

			System.out.println(request.contentType());
			request.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));
			Part part = request.raw().getPart("file");
			
			try (InputStream input = part.getInputStream()) {
				Files.copy(input, tempFile, StandardCopyOption.REPLACE_EXISTING);
			}
			AWSCredentials credentials = null;
			ProfileCredentialsProvider credentialsProvider = new ProfileCredentialsProvider();
	        try {
	            credentials = new ProfileCredentialsProvider("default").getCredentials();
	            credentialsProvider.getCredentials();
	        } catch (Exception e) {
	            throw new AmazonClientException(
	                    "Cannot load the credentials from the credential profiles file. " +
	                    "Please make sure that your credentials file is at the correct " +
	                    "location (C:\\Users\\Jahnavi\\.aws\\credentials), and is in valid format.",
	                    e);
	        }
	        
	        AmazonS3 s3 = AmazonS3ClientBuilder.standard()
	                .withCredentials(new AWSStaticCredentialsProvider(credentials))
	                .withRegion("us-east-2")
	                .build();
	        String bucketName = "globalcustbucket";
	        System.out.println("Uploading a new object to S3 from a file\n");
	        s3.putObject(new PutObjectRequest(bucketName, part.getSubmittedFileName(), tempFile.toFile()));
	        
			logInfo(request, tempFile);
			return "<h1>You uploaded this image:<h1><img src='" + tempFile.getFileName() + "'>";
		});
	}

	// methods used for logging
	private static void logInfo(Request req, Path tempFile) throws IOException, ServletException {
		System.out.println("Uploaded file '" + getFileName(req.raw().getPart("file")) + "' saved as '"
				+ tempFile.toAbsolutePath() + "'");
	}

	private static String getFileName(Part part) {
		for (String cd : part.getHeader("content-disposition").split(";")) {
			if (cd.trim().startsWith("filename")) {
				return cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
			}
		}
		return null;
	}

}
