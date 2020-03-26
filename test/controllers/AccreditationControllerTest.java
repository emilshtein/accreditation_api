package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.AccreditationRequest;
import models.Document;
import models.Payload;
import org.junit.Test;
import play.Application;
import play.inject.guice.GuiceApplicationBuilder;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import play.test.WithApplication;
import scala.util.parsing.json.JSONArray;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.*;

public class AccreditationControllerTest extends WithApplication {

    @Override
    protected Application provideApplication() {
        return new GuiceApplicationBuilder().build();
    }

    @Test
    public void testAccredit() throws IOException {

        byte[] jsonData = Files.readAllBytes(Paths.get("test/resources/SampleRequest.json"));
        //create ObjectMapper instance
        ObjectMapper objectMapper = new ObjectMapper();
        //read JSON like DOM Parser
        JsonNode rootNode = objectMapper.readTree(jsonData);

        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(POST)
                .bodyJson(rootNode)
                .uri("/user/accreditation");

        Result result = route(app, request);
        assertEquals(CREATED, result.status());
        assertTrue(result.contentType().isPresent());
        assertEquals("application/json", result.contentType().get());
    }

    @Test
    public void testAccreditMissingField() {
        final ObjectNode jsonNode = Json.newObject();
        jsonNode.put("user_id", "John");

        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(POST)
                .bodyJson(jsonNode)
                .uri("/user/accreditation");

        Result result = route(app, request);
        assertEquals(INTERNAL_SERVER_ERROR, result.status());
        assertTrue(result.contentType().isPresent());
        assertEquals("application/json", result.contentType().get());
    }
}
