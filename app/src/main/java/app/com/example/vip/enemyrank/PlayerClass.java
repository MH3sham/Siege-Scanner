package app.com.example.vip.enemyrank;


public class PlayerClass {
    String imageProfile;
    String name;
    String playerlvl;
    String playerKD;
    String imageRank;
    String rankName;

    public PlayerClass(String imageProfile, String name , String playerlvl, String playerKD, String imageRank, String rankName){
        this.imageProfile = imageProfile;
        this.name = name;
        this.playerlvl = playerlvl;
        this.playerKD = playerKD;
        this.imageRank = imageRank;
        this.rankName = rankName;
    }

    public String getImageProfile() {
        return imageProfile;
    }

    public String getName() {
        return name;
    }

    public String getPlayerlvl() {
        return playerlvl;
    }

    public String getPlayerKD() {
        return playerKD;
    }

    public String getImageRank() {
        return imageRank;
    }

    public String getRankName() {
        return rankName;
    }
}
