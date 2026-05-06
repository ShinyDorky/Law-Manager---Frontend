package shinydorky.mos.law_generator_frontend.rest;

import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import shinydorky.mos.law_generator_frontend.dto.LawGroupDto;
import shinydorky.mos.law_generator_frontend.dto.LawOptionDto;
import shinydorky.mos.law_generator_frontend.dto.LawTypeDto;
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
            lawType.setChildGroups(new ArrayList<>());
            result.add(lawType);
        });
        return result;
    }

    public static ArrayList<LawGroup> GetGroupsInType(LawType type) throws ResourceAccessException {
        RestTemplate restTemplate = new RestTemplate();

        ArrayList<LawGroup> result = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode data = restTemplate.getForObject(address + "/lawType/"+ type.getId() + "/lawGroups", JsonNode.class);
        data.forEach(group -> {
            LawGroup lawGroup = mapper.readValue(group.toString(), LawGroup.class);
            lawGroup.setParentLawType(type);
            type.getChildGroups().add(lawGroup);
            lawGroup.setChildOptions(new ArrayList<>());
            result.add(lawGroup);
        });
        return result;
    }

    public static ArrayList<LawOption> GetOptionsInGroup(LawGroup group) throws ResourceAccessException {
        RestTemplate restTemplate = new RestTemplate();

        ArrayList<LawOption> result = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode data = restTemplate.getForObject(address + "/lawGroup/"+ group.getId() + "/lawOptions", JsonNode.class);
        data.forEach(option -> {
            LawOption lawOption = mapper.readValue(option.toString(), LawOption.class);
            lawOption.setParentLawGroup(group);
            group.getChildOptions().add(lawOption);
            result.add(lawOption);
        });
        return result;
    }

    public static void createNewLawType(LawTypeDto lawType){
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForObject(address + "/lawType", lawType, JsonNode.class);
    }
    public static void createNewLawGroup(LawGroupDto lawGroup){
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForObject(address + "/lawGroup/" + lawGroup.getLawTypeId(), lawGroup, JsonNode.class);
    }
    public static void createNewLawOption(LawOptionDto lawOption){
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForObject(address + "/lawOption/" + lawOption.getLawGroupId(), lawOption, JsonNode.class);
    }

    public static void updateLawType(LawTypeDto lawType){
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.put(address + "/lawType/" + lawType.getId(), lawType, JsonNode.class);
    }
    public static void updateLawGroup(LawGroupDto lawGroup){
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.put(address + "/lawGroup/" + lawGroup.getId(), lawGroup, JsonNode.class);
    }
    public static void updateLawOption(LawOptionDto lawOption){
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
