package ui.nodes.toggle;

import javafx.event.Event;
import javafx.event.EventType;

public class StateChangeEvent extends Event {

    private final ToggleState state;

    public StateChangeEvent(EventType<? extends Event> eventType, ToggleState state) {
        super(eventType);
        this.state = state;
    }

    public ToggleState getState() {
        return state;
    }

}
