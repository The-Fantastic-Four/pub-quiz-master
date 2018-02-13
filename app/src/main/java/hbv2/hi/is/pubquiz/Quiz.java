package hbv2.hi.is.pubquiz;

/**
 * Created by Fannar on 9.2.2018.
 */

public class Quiz {

    private String id;
    private String roomName;
    private String hostName;

    public Quiz(String id, String roomName, String hostName) {
        this.id = id;
        this.roomName = roomName;
        this.hostName = hostName;
    }

    public Quiz(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }




}
