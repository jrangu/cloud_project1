package server;

import static spark.Spark.awaitInitialization;
import static spark.Spark.get;

import api.JsonTransformer;
import controller.UserFileRelationController;

public class Main {
	
	public static void main(String[] args) {
		get("/fileList", (request, response) -> {
			return UserFileRelationController.getFileList(request, response);
		}, new JsonTransformer());

//		post("/upload", (request, response) -> {
//			return UserFileRelationController.uploadFile(request, response);
//		}, new JsonTransformer());
		awaitInitialization();
	}
}
