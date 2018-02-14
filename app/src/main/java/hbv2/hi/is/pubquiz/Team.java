package hbv2.hi.is.pubquiz;

/**
 * Created by Fannar on 9.2.2018.
 */

//Model class for Team
public class Team {

    private String teamName;
    private String roomName;

    public Team(String teamName, String roomName){

        this.teamName = teamName;
        this.roomName = roomName;
    }

    public Team(){

    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }


    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }
}
