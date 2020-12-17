package dqa.com.msibook;

import java.io.Serializable;

public class msibook_requset_form_item implements Serializable {

    String F_SeqNo;
    String F_CreateDate;
    String Status;
    String Model;
    String F_Memo;
    String F_ExpectFixDate;
    String AssignUser;
    String RequestType;


    public msibook_requset_form_item(String F_SeqNo, String F_CreateDate, String Status, String Model, String F_Memo, String F_ExpectFixDate, String AssignUser, String RequestType) {

        this.F_SeqNo = F_SeqNo;
        this.F_CreateDate = F_CreateDate;
        this.Status = Status;
        this.Model = Model;
        this.F_Memo = F_Memo;
        this.F_ExpectFixDate = F_ExpectFixDate;
        this.AssignUser = AssignUser;
        this.RequestType = RequestType;

    }
}
