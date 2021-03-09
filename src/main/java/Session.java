public class Session {

    private int maxDor;               // creates object fields
    private int maxPla;
    private int maxInv;
    private int maxEve;
    private int DorCount;
    private int PlaCount;
    private int InvCount;
    private int EveCount;
    private double dif;
    private String fname;
    private String lname;

    // creates session object
    public Session(String fname , String lname){
        this.fname = fname;
        this.lname = lname;
    }

    // defines methods to get object info
    public int getMaxDor(){
        return maxDor;
    }

    public int getMaxPla(){
        return maxPla;
    }

    public int getMaxInv(){
        return maxInv;
    }

    public int getMaxEve(){
        return maxEve;
    }

    public String getFname(){
        return fname;
    }

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

    public void setDor(int dor) {
        this.maxDor = dor;
    }
    public void setPla(int pla) {
        this.maxPla = pla;
    }
    public void setInv(int inv) {
        this.maxInv = inv;
    }
    public void setEve(int eve) {
        this.maxEve = eve;
    }
}
