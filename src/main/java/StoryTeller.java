import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StoryTeller {

    private final StoryReader.Game model;
    private final Console view;
    private String roomId;
    private List<String> states = new ArrayList<>();
    private List<String> toAdd = new ArrayList<>();
    private boolean skip;

    public StoryTeller(StoryReader.Game model, Console view) {
        this.model = model;
        this.view = view;
        roomId = model.startRoom();
    }

    public void tellStory() throws IOException {
        view.print(model.description());

        while (true) {
            skip = false;

            Map<String, List<StoryReader.Game.Room.Action>> maybeVerb = new HashMap<>();
            List<StoryReader.Game.Room.Action> maybeObject = new ArrayList<>();

            var room = model.rooms().get(roomId);
            view.print(room.description());

            var input = view.getInput();

            maybeVerb = room.verbs().get(input.verb());
            if (maybeVerb != null) {
                maybeObject = maybeVerb.get(input.object());
            }
            if (maybeObject != null) {
                for (var object : maybeObject) {
                    if (object.ifState() != null) {
                        for (var state : states) {
                            if (state.equals(object.ifState())) {
                                skip = true;
                                if (object.message() != null) {
                                    view.print(object.message());
                                }
                                if (object.addState() != null) {
                                    toAdd.add(object.addState());
                                }
                                if (object.room() != null) {
                                    roomId = object.room();
                                }
                            }
                        }
                        states.addAll(toAdd);
                        toAdd.clear();
                    } else if (object.addState() != null) {
                        if (states.size() == 0) {
                            states.add(object.addState());
                            view.print(object.message());
                        }
                        for (var state : states) {
                            if (!state.equals(object.addState())) {
                                states.add(object.addState());
                                view.print(object.message());
                            }
                        }
                    } else if (!skip) {
                        if (object.message() != null) {
                            view.print(object.message());
                        }
                        if (object.room() != null) {
                            roomId = object.room();
                        }
                    }
                }
            }

            if (input.verb().equals("inventory")) {
                view.printInventory(states);
            } else if (input.verb().equals("help")) {
                if(input.object().equals("verbs")){
                    view.printVerbs(room.verbs());
                }
                else {
                    view.printError(model.verbs().get("help").errors().verb());
                    view.printError(model.verbs().get("help").errors().object());
                }
            } else if (maybeVerb == null || maybeVerb.size() == 0) {
                var maybeGeneralVerb = model.verbs().get(input.verb());
                if (maybeGeneralVerb != null) {
                    view.printError(model.verbs().get(input.verb()).errors().verb());
                } else {
                    view.printError(model.verbs().get("default").errors().verb());
                }
            } else if (maybeObject == null || maybeObject.size() == 0) {
                view.printError(model.verbs().get(input.verb()).errors().object());
            }
        }
    }
}
