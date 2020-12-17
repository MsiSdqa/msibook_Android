package dqa.com.msibook;

public class Declare_Month_Item {

    String F_TotalHour;//申請總時數
    String Date; //當日日期

    public Declare_Month_Item(String Date,String F_TotalHour)
    {
        this.Date = Date;

        this.F_TotalHour = F_TotalHour;
    }

    public String Date()
    {
        return this.Date;
    }

    public String F_TotalHour()
    {
        return this.F_TotalHour;
    }

}
