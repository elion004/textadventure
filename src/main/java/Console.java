import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

public class Console {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_YELLOW = "\u001B[33m";

    public BufferedReader Reader;

    public record Input(String verb, String object) {};

    public Input getInput() throws IOException {
        this.Reader = new BufferedReader(new InputStreamReader(System.in));

        var word = Reader.readLine().trim().split(" ");

        if(word.length == 1){
            return new Input(word[0], "");
        }
        return new Input(word[0], word[1]);
    }

    public void print(String description) {
        System.out.println(ANSI_PURPLE + description + ANSI_RESET + "\n");
    }

    public void printError(String description) {
        System.out.println(ANSI_RED + description + ANSI_RESET + "\n");
    }

    public void printInventory(List<String> states){
        if(states.size() == 0){
            System.out.println(ANSI_YELLOW + "empty :(" + ANSI_RESET);
        }
        for (var item : states) {
            System.out.println(ANSI_YELLOW + "-" + item + ANSI_RESET);
        }
        System.out.println(ANSI_YELLOW + "----------------------" + ANSI_RESET);
    }

    public void printVerbs(Map<String, Map<String, List<StoryReader.Game.Room.Action>>> verbs) {
        System.out.println(ANSI_RED + "You can use:" + ANSI_RESET);
        for (Map.Entry<String, Map<String, List<StoryReader.Game.Room.Action>>> entry : verbs.entrySet())
            System.out.println(ANSI_RED + "    " + entry.getKey() + ANSI_RESET);
    }
}
