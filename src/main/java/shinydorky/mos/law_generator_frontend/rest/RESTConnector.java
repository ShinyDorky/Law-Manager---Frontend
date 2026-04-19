package shinydorky.mos.law_generator_frontend.rest;

import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;
import shinydorky.mos.law_generator_frontend.model.LawType;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.ArrayList;

public class RESTConnector {
    private final static String address = "http://localhost:8080/api";

    public static ArrayList<LawType> getAllTypes(){
        RestTemplate restTemplate = new RestTemplate();

        ArrayList<LawType> result = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode data = restTemplate.getForObject(address + "/lawType", JsonNode.class);
        ArrayList<String> types = new ArrayList<>();
        data.forEach(type -> {
            types.add(type.toString());
            LawType lawType = mapper.readValue(type.toString(), LawType.class);
            result.add(lawType);
        });
        return result;
    }
}
