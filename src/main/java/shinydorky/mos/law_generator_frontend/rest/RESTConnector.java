package shinydorky.mos.law_generator_frontend.rest;

import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;
import shinydorky.mos.law_generator_frontend.model.LawGroup;
import shinydorky.mos.law_generator_frontend.model.LawOption;
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
        data.forEach(type -> {
            LawType lawType = mapper.readValue(type.toString(), LawType.class);
            result.add(lawType);
        });
        return result;
    }

    public static ArrayList<LawGroup> getGroupsInType(long typeId){
        RestTemplate restTemplate = new RestTemplate();

        ArrayList<LawGroup> result = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode data = restTemplate.getForObject(address + "/lawType/"+ typeId + "/lawGroups", JsonNode.class);
        data.forEach(type -> {
            LawGroup lawGroup = mapper.readValue(type.toString(), LawGroup.class);
            result.add(lawGroup);
        });
        return result;
    }

    public static ArrayList<LawOption> getOptionsInGroup(long groupId){
        RestTemplate restTemplate = new RestTemplate();

        ArrayList<LawOption> result = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode data = restTemplate.getForObject(address + "/lawGroup/"+ groupId + "/lawOptions", JsonNode.class);
        data.forEach(type -> {
            LawOption lawGroup = mapper.readValue(type.toString(), LawOption.class);
            result.add(lawGroup);
        });
        return result;
    }
}
