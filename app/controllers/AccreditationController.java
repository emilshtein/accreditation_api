package controllers;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import models.AccreditationRequest;
import models.Document;
import models.Payload;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import utils.Util;

import javax.inject.Inject;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.util.concurrent.CompletableFuture.supplyAsync;

/*
We'll implement our actions using asynchronous, non-blocking code.
This means that our action methods will return CompletionStage<Result> instead of just Result.
This has the benefit of allowing us to write long-running tasks without blocking.
 */
public class AccreditationController extends Controller {
    private HttpExecutionContext ec;

    //Validator for incoming JSON
    @Inject
    Validator validator;

    @Inject
    public AccreditationController(HttpExecutionContext ec) {
        this.ec = ec;
    }


    public CompletionStage<Result> accredit(Http.Request request) {
        JsonNode json = request.body().asJson();
        return supplyAsync(() -> {
            AtomicBoolean wasAccredited = new AtomicBoolean(false);
            if (json == null) {
                //the POST has wrong Content-Type
                return badRequest(Util.createResponse("Expecting Json data", false, wasAccredited.get()));
            }
            //Try to build AccreditationRequest from incoming JSON. If the request is not valid null will be returned.
            Optional<AccreditationRequest> accreditationRequestOptional = Optional.ofNullable(getValidAccreditationRequestFromJSON(json));
            return accreditationRequestOptional.map(accreditationRequest -> {
                // Accreditation part
                // for this test it will use 70% success rate
                wasAccredited.set(Util.generateRandomAnswer());
                //
                return created(Util.createResponse("",true, wasAccredited.get()));
            }).orElse(internalServerError(Util.createResponse("Could not create accreditation data.", false, wasAccredited.get())));
        }, ec.current());
    }

    public AccreditationRequest getValidAccreditationRequestFromJSON(JsonNode json) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String inputJson = objectMapper.writeValueAsString(json);
            AccreditationRequest accreditationRequest = objectMapper.readValue(inputJson, AccreditationRequest.class);
            if (accreditationRequestIsValid(accreditationRequest)) {
                return accreditationRequest;
            }
        } catch (JsonProcessingException e) {
            //JSON deserialization failed
        }
        return null;
    }

    public boolean accreditationRequestIsValid(AccreditationRequest accreditationRequest) {
        //validate the AccreditationRequest object
        Set<ConstraintViolation<AccreditationRequest>> violations = validator.validate(accreditationRequest);
        if (violations.isEmpty()) {
            //validate the Payload object and each document in it
            Set<ConstraintViolation<Payload>> violationsInPayload = validator.validate(accreditationRequest.getPayload());
            if (violationsInPayload.isEmpty() && documentsValid(accreditationRequest)) {
                return true;
            }
        }
        return false;
    }


    public boolean documentsValid(AccreditationRequest accreditationRequest) {
        for(Document document:accreditationRequest.getPayload().getDocuments()) {
            Set<ConstraintViolation<Document>> violations = validator.validate(document);
            if (!violations.isEmpty())
                return false;
        }
        return true;
    }

}
