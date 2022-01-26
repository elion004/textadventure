import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class StoryReader {

    @JsonIgnoreProperties(ignoreUnknown = true)
    record Game(String startRoom,String description, Map<String, Room> rooms, Map<String, addInfos> verbs) {

        record Room(String name, String description, Map<String, Map<String, List<Action>>> verbs) {

            @JsonIgnoreProperties(ignoreUnknown = true)
            record Action(String room, String message, String ifState, String addState) {
            }
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        record addInfos(List<String> synonyms , Error errors){}

        record Error(String verb, String object){}

    }

    public static Game read() throws IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        mapper.findAndRegisterModules();
        return mapper.readValue(new File("src/tutorial-211205-83119c1.yaml"), Game.class);
    }
}
