import java.io.IOException;
public class Main {

    public static void main(String[] args) throws IOException {
        StoryReader.Game game = StoryReader.read();
        Console view = new Console();
        StoryTeller teller = new StoryTeller(game, view);
        teller.tellStory();
    }
}
