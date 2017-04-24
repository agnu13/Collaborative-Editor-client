package ui.nodes.toggle;

public enum ToggleState {

    ON, OFF;

    ToggleState toggle() {
        if (this == ON) {
            return OFF;
        }

        return ON;
    }
}
