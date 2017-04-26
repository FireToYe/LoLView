package cn.ycl.com.lolview;

/**
 * Created by yechenglong on 2017/4/19.
 */

public class MoneyBean {
    private String money;
    private String time;

    public MoneyBean(String money, String time) {
        this.money = money;
        this.time = time;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
