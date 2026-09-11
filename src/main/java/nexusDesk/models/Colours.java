package nexusDesk.models;

public class Colours {

    private int colour_id;
    private String colour;
    private String hex;

    public Colours(int colour_id, String colour, String hex) {
        this.colour_id = colour_id;
        this.colour = colour;
        this.hex = hex;
    }

    public int getColour_id() {
        return colour_id;
    }
    public void setColour_id(int colour_id) {
        this.colour_id = colour_id;
    }

    public String getColour() {
        return colour;
    }
    public void setColour(String colour) {
        this.colour = colour;
    }

    public String getHex() {
        return hex;
    }
    public void setHex(String hex) {
        this.hex = hex;
    }
}
