public class Session {

    private double gameMax;               // creates object fields
    private double gameMin;
    private double testMax;
    private double testMin;
    private int maxCount;
    private int minCount;
    private double gameScore;
    private double testScore;
    private double gameTime;
    private double dif;
    private String fname;
    private String lname;


    // creates session object
    public Session(String fname , String lname){
        this.fname = fname;
        this.lname = lname;
        this.gameMax = 0;
        this.gameMin = 0;
        this.maxCount = 0;
        this.minCount = 0;
        this.gameScore = 0;
        this.gameTime = 0;

    }

    // defines methods to get object info
    public double getGameMax(){
        return gameMax;
    }

    public double getGameMin(){
        return gameMin;
    }

    public int getMaxCount(){ return maxCount; }

    public int getMinCount(){ return minCount; }

    public double getGameScore(){ return gameScore; }

    public double getGameTime(){ return gameTime; }

    public String getFname(){ return fname; }

    public String getLname(){
        return lname;
    }

    public double getDiff(){
        return dif;
    }

    // defines methods to set object variables
    public void setDiff(double difficulty) {
        this.dif = difficulty;
    }

    public void setGameMax(double max) {
        this.gameMax = max;
    }
    public void setGameMin(double min) {
        this.gameMin = min;
    }
    public void setMaxCount(int upCount) {
        this.maxCount = upCount;
    }
    public void setMinCount(int dCount) {
        this.minCount = dCount;
    }
    public void setGameScore(double gScore) {
        this.gameScore = gScore;
    }
    public void setGameTime(double time) {
        this.gameTime = time;
    }

}
