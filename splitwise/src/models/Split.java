package models;

public class Split {
    private Users user;

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    private int totalamt;
    private int percentage;

    public Split(Users user, int totalamt, int percentage) {
        this.user = user;
        this.totalamt = totalamt;
        this.percentage = percentage;
    }

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }

    public int amt(){
        // System.out.println(percentage / 100);
        return (percentage* totalamt)/100;
    }

}
