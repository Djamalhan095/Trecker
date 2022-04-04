public class Subtask extends Task {
    private int epicID;

    public Subtask(String name, String description, String status, int epicID) {
        super(name, description, status);
        this.epicID = epicID;
    }

    public int getEpicID() {
        return epicID;
    }
}
