package nexusDesk.models;

public class Colour {

    private int colour_id;
    private String colour;
    private String hex;

    public Colour(int colour_id, String colour, String hex) {
        this.colour_id = colour_id;
        this.colour = colour;
        this.hex = hex;
    }

    public int getColourId() {
        return colour_id;
    }
    public void setColourId(int colour_id) {
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
