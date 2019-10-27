package api;

import com.google.gson.Gson;
import spark.ResponseTransformer;

/**
 * Converts java objects to json representation.
 */
public class JsonTransformer implements ResponseTransformer {

  private Gson gson = new Gson();

  @Override
  public String render(Object model) {
    return gson.toJson(model);
  }

}