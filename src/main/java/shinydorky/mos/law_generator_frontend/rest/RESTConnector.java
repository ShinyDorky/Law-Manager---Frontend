package shinydorky.mos.law_generator_frontend.rest;

import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import shinydorky.mos.law_generator_frontend.model.LawGroup;
import shinydorky.mos.law_generator_frontend.model.LawOption;
import shinydorky.mos.law_generator_frontend.model.LawType;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.ArrayList;

public class RESTConnector {
    private final static String address = "http://localhost:8080/api";

    public static ArrayList<LawType> GetAllTypes() throws ResourceAccessException {
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

    public static ArrayList<LawGroup> GetGroupsInType(long typeId) throws ResourceAccessException {
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

    public static ArrayList<LawOption> GetOptionsInGroup(long groupId) throws ResourceAccessException {
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

    public static void createNewLawType(LawType lawType){
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForObject(address + "/lawType", lawType, JsonNode.class);
    }
    public static void createNewLawGroup(LawGroup lawGroup){
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForObject(address + "/lawGroup/" + lawGroup.getLawTypeId(), lawGroup, JsonNode.class);
    }
    public static void createNewLawOption(LawOption lawOption){
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForObject(address + "/lawOption/" + lawOption.getLawGroupId(), lawOption, JsonNode.class);
    }

    public static void updateLawType(LawType lawType){
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.put(address + "/lawType/" + lawType.getId(), lawType, JsonNode.class);
    }
    public static void updateLawGroup(LawGroup lawGroup){
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.put(address + "/lawGroup/" + lawGroup.getId(), lawGroup, JsonNode.class);
    }
    public static void updateLawOption(LawOption lawOption){
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.put(address + "/lawOption/" + lawOption.getId(), lawOption, JsonNode.class);
    }

    public static void DeleteLawType(Long id){
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.delete(address + "/lawType/" + id, JsonNode.class);
    }
    public static void DeleteLawGroup(Long id){
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.delete(address + "/lawGroup/" + id, JsonNode.class);
    }
    public static void DeleteLawOption(Long id){
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.delete(address + "/lawOption/" + id, JsonNode.class);
    }
}
