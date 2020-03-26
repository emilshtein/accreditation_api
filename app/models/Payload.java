package models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.validation.constraints.NotNull;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "accreditation_type",
        "documents"
})
public class Payload {

    @JsonProperty("accreditation_type")
    @NotNull
    private String accreditationType;
    @JsonProperty("documents")
    @NotNull
    private List<Document> documents = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Payload() {
    }

    public Payload(String accreditationType, List<Document> documents) {
        super();
        this.accreditationType = accreditationType;
        this.documents = documents;
    }

    @JsonProperty("accreditation_type")
    public String getAccreditationType() {
        return accreditationType;
    }

    @JsonProperty("accreditation_type")
    public void setAccreditationType(String accreditationType) {
        this.accreditationType = accreditationType;
    }

    @JsonProperty("documents")
    public List<Document> getDocuments() {
        return documents;
    }

    @JsonProperty("documents")
    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
