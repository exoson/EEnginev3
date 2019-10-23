package client.game;

import java.util.ArrayList;

public class UiObject {

    private boolean isDisplayed;
    private ArrayList<UiObject> children;

    public UiObject() {
        children = new ArrayList<>();
    }

    public void update() {
        for (UiObject child : children) {
            child.update();
        }
    }

    public void render() {
        for (UiObject child : children) {
            if (isDisplayed) {
                child.render();
            }
        }
    }

    public void addChild(UiObject child) {
        children.add(child);
    }

    public void setVisibility(boolean visibility) {
        isDisplayed = visibility;
    }
}
