import java.util.HashMap;
import java.util.Map;

public class machine {
    private States states;
    Map<String, state> map = new HashMap<>();
    public machine() {
        map.put("DISPENSE", new dispensing());
        map.put("SELECTION", new selection());
        // Initialize with default state
        this.states = States.SELECTION;
    }
}
