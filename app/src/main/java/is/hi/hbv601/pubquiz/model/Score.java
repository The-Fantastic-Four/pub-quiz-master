package is.hi.hbv601.pubquiz.model;

public class Score {
    private Long place;
    private String teamName;
    private Long score;

    public Score(Long place, String teamName, Long score) {
        this.place = place;
        this.teamName = teamName;
        this.score = score;
    }

    public Long getPlace() {
        return place;
    }

    public void setPlace(Long place) {
        this.place = place;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public Long getScore() {
        return score;
    }

    public void setScore(Long score) {
        this.score = score;
    }
}
