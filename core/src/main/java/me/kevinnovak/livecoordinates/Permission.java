package me.kevinnovak.livecoordinates;

public enum Permission {
    DISPLAY("livecoordinates.display");

    private String _text;

    Permission(String text) {
        _text = text;
    }

    public String getText() {
        return _text;
    }
}
