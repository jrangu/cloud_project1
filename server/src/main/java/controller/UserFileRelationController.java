package controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Set;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import javax.servlet.http.Part;

import org.apache.commons.io.FileUtils;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import api.ErrorResponse;
import db.DatabaseConnectionManager;
import db.UserFileRelationDb;
import model.UserFileRelation;
import spark.Request;
import spark.Response;

/**
 * Controller class for <i>prescription</i> end point.
 */
public class UserFileRelationController {
	private static Gson gson = new GsonBuilder().create();
	private static Regions clientRegion = Regions.US_EAST_2;
	private static String bucketName = "globalcustbucket";
	private static AmazonS3 s3Client;

	static {
		s3Client = AmazonS3ClientBuilder.standard().withCredentials(new ProfileCredentialsProvider())
				.withRegion(clientRegion).build();
	}

	public static Object getFileList(Request request, Response response) throws SQLException {
		response.type("application/json");
		try (Connection connection = DatabaseConnectionManager.getConnection()) {
			Set<UserFileRelation> prescription = new UserFileRelationDb(connection)
					.get(request.queryParams("user_name"));
			if (!prescription.isEmpty()) {
				return prescription;
			} else {
				return new ErrorResponse().setErrorMessage("Prescription not found");
			}
		}
	}

	public static Object uploadFile(Request request, Response response)
			throws IOException, ServletException, SQLException {
		response.type("application/json");
		request.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));
		Part part = request.raw().getPart("file");
		File scratchFile = File.createTempFile("prefix", "suffix");

//		String file_key = UUID.randomUUID().toString();
		String userName = null;
		String firstName = null;
		String lastName = null;
		String fileDesc = null;
		String fileName = part.getSubmittedFileName();

//		AWSCredentials credentials = new ProfileCredentialsProvider("default").getCredentials();
//		ProfileCredentialsProvider credentialsProvider = new ProfileCredentialsProvider();
//		credentialsProvider.getCredentials();
//		AmazonS3 s3 = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials))
//				.withRegion("us-east-2").build();
		try (InputStream input = part.getInputStream()) {
			FileUtils.copyInputStreamToFile(input, scratchFile);
		}
		part = request.raw().getPart("user_name");
		try (InputStream input = part.getInputStream()) {
			userName = IOUtils.toString(input);
		}
		part = request.raw().getPart("first_name");
		try (InputStream input = part.getInputStream()) {
			firstName = IOUtils.toString(input);
		}
		part = request.raw().getPart("last_name");
		try (InputStream input = part.getInputStream()) {
			lastName = IOUtils.toString(input);
		}
		part = request.raw().getPart("file_desc");
		try (InputStream input = part.getInputStream()) {
			fileDesc = IOUtils.toString(input);
		}
		s3Client.putObject(new PutObjectRequest(bucketName, fileName, scratchFile));

		try (Connection connection = DatabaseConnectionManager.getConnection()) {
			UserFileRelationDb prescriptionDb = new UserFileRelationDb(connection);
			Timestamp timeStamp = new Timestamp(
					s3Client.getObjectMetadata(bucketName, fileName).getLastModified().getTime());
			UserFileRelation userFileRelation = new UserFileRelation().setUserId(userName).setFirstName(firstName)
					.setLastName(lastName).setFileName(fileName).setFileDesc(fileDesc).setUpdatedTimestamp(timeStamp)
					.setCreatedTimestamp(timeStamp);
			prescriptionDb.upload(userFileRelation);
			return new UserFileRelationDb(connection).get(userName);
		}
	}

	public static Object deleteFile(Request request, Response response) throws SQLException {
		String userName = request.queryParams("user_name");
		String fileKey = request.queryParams("file_key");
		s3Client.deleteObject(new DeleteObjectRequest(bucketName, fileKey));

		response.type("application/json");
		try (Connection connection = DatabaseConnectionManager.getConnection()) {
			new UserFileRelationDb(connection).delete(userName, fileKey);
			return "Success";
		}
	}

//  public static Object create(Request request, Response response) throws SQLException {
//    response.type("application/json");
//    try(Connection connection = DatabaseConnectionManager.getConnection()) {
//      UserFileRelationDb prescriptionDb = new UserFileRelationDb(connection);
//      UserFileRelation prescription = readPrescriptionFromRequest(request);
//      Optional<ErrorResponse> validationErrors = validate(prescription);
//      if(validationErrors.isPresent()) {
//        return validationErrors;
//      }
//      prescriptionDb.create(createToken(prescription));
//      return new UserFileRelationDb(connection).get(prescription.getToken());
//    }
//  }
//
//  private static UserFileRelation readPrescriptionFromRequest(Request request) {
//    return gson.fromJson(request.body(), UserFileRelation.class);
//  }
//
//  private static Optional<ErrorResponse> validate(UserFileRelation prescription) {
//    if(isEmpty(prescription.getUserId())) {
//      return Optional.of(new ErrorResponse().setErrorCode("User id is required"));
//    }
//    if(isEmpty(prescription.getFileKey())) {
//      return Optional.of(new ErrorResponse().setErrorCode("File key is required"));
//    }
//    return Optional.empty();
//  }
//
//  private static UserFileRelation createToken(UserFileRelation prescription) {
//    return prescription.setToken(UUID.randomUUID().toString());
//  }

}
