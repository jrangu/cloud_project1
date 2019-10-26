package server;

import static spark.Spark.awaitInitialization;
import static spark.Spark.before;
import static spark.Spark.get;
import static spark.Spark.options;
import static spark.Spark.post;

import api.JsonTransformer;
import controller.UserFileRelationController;

public class Main {

	public static void main(String[] args) {
		options("/*", (request, response) -> {
			String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
			if (accessControlRequestHeaders != null) {
				response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
			}

			String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
			if (accessControlRequestMethod != null) {
				response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
			}

			return "OK";
		});
		before((request, response) -> {
			response.header("Access-Control-Allow-Origin", "*");
			response.header("Access-Control-Request-Method", "*");
			response.header("Access-Control-Allow-Headers", "*");
			response.type("application/json");
		});
		get("/fileList", (request, response) -> {
			System.out.println("get file list");
			return UserFileRelationController.getFileList(request, response);
		}, new JsonTransformer());

		post("/delete", (request, response) -> {
			System.out.println("delete file");
			return UserFileRelationController.deleteFile(request, response);
		}, new JsonTransformer());
		post("/upload", (request, response) -> {
			System.out.println("upload file");
			return UserFileRelationController.uploadFile(request, response);
		}, new JsonTransformer());
		awaitInitialization();
	}
}
