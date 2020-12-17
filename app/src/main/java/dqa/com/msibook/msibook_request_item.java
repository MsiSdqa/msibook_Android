package dqa.com.msibook;

import java.io.Serializable;

public class msibook_request_item implements Serializable {

    String F_SeqNo;
    String IMG;
    String F_NO;
    String F_CreateDate;
    String F_UpdateTime;
    String F_Keyin;
    String F_Owner;
    String F_Status;
    String Status;
    String F_Subject;
    String F_Desc;
    String F_Grade;
    String Grade;
    String F_Grade_Note;
    String F_AssinUser;
    String AssinUser;
    String F_RespUser;
    String RespUser;
    String F_SDate;
    String F_EDate;
    String F_ExpectFixDate;
    String F_Master_Table;
    String F_Master_ID;
    String DueDays;
    String OverDueDays;
    String Is_Over;
    String Favorite;

    public msibook_request_item(String F_SeqNo,String IMG,String F_NO,String F_CreateDate,String F_UpdateTime,String F_Keyin,String F_Owner,String F_Status,String Status,String F_Subject,String F_Desc,String F_Grade,String Grade,String F_Grade_Note,String F_AssinUser,String AssinUser,String F_RespUser,String RespUser,String F_SDate,String F_EDate,String F_ExpectFixDate,String F_Master_Table,String F_Master_ID,String DueDays,String OverDueDays,String Is_Over,String Favorite) {

        this.F_SeqNo = F_SeqNo;
        this.IMG = IMG;
        this.F_NO = F_NO;
        this.F_CreateDate = F_CreateDate;
        this.F_UpdateTime = F_UpdateTime;
        this.F_Keyin = F_Keyin;
        this.F_Owner = F_Owner;
        this.F_Status = F_Status;
        this.Status = Status;
        this.F_Subject = F_Subject;
        this.F_Desc = F_Desc;
        this.F_Grade = F_Grade;
        this.Grade = Grade;
        this.F_Grade_Note = F_Grade_Note;
        this.F_AssinUser = F_AssinUser;
        this.AssinUser = AssinUser;
        this.F_RespUser = F_RespUser;
        this.RespUser = RespUser;
        this.F_SDate = F_SDate;
        this.F_EDate = F_EDate;
        this.F_ExpectFixDate = F_ExpectFixDate;
        this.F_Master_Table = F_Master_Table;
        this.F_Master_ID = F_Master_ID;
        this.DueDays = DueDays;
        this.OverDueDays = OverDueDays;
        this.Is_Over = Is_Over;
        this.Favorite = Favorite;
    }

}
