package dqa.com.msibook;

import java.io.Serializable;

public class msibook_ehr_control_job_item implements Serializable {

    String F_SeqNo;
    String F_CreateDate;
    String F_UpdateDate;
    String F_Keyin;
    String F_Stat;
    String F_Job_Name;
    String F_Job_Content;
    String F_Job_InterView;
    String F_Job_gender;
    String F_Job_Level_Base;
    String F_Job_Level_Top;
    String F_Job_Age_Base;
    String F_Job_Age_Top;
    String F_Job_People;
    String F_Status;
    String DeptName;
    String F_DeptCode;

    public msibook_ehr_control_job_item(String F_SeqNo,String F_CreateDate,String F_UpdateDate,String F_Keyin,String F_Stat,String F_Job_Name,String F_Job_Content,String F_Job_InterView,String F_Job_gender,String F_Job_Level_Base,String F_Job_Level_Top,String F_Job_Age_Base,String F_Job_Age_Top,String F_Job_People,String F_Status,String DeptName,String F_DeptCode)
    {
        this.F_SeqNo = F_SeqNo;
        this.F_CreateDate = F_CreateDate;
        this.F_UpdateDate = F_UpdateDate;
        this.F_Keyin = F_Keyin;
        this.F_Stat = F_Stat;
        this.F_Job_Name = F_Job_Name;
        this.F_Job_Content = F_Job_Content;
        this.F_Job_InterView = F_Job_InterView;
        this.F_Job_gender = F_Job_gender;
        this.F_Job_Level_Base = F_Job_Level_Base;
        this.F_Job_Level_Top = F_Job_Level_Top;
        this.F_Job_Age_Base = F_Job_Age_Base;
        this.F_Job_Age_Top = F_Job_Age_Top;
        this.F_Job_People = F_Job_People;
        this.F_Status = F_Status;
        this.DeptName = DeptName;
        this.F_DeptCode = F_DeptCode;
    }
    public String GetF_SeqNo()
    {
        return this.F_SeqNo;
    }
    public String GetF_CreateDate()
    {
        return this.F_CreateDate;
    }
    public String GetF_UpdateDate()
    {
        return this.F_UpdateDate;
    }
    public String GetF_Keyin()
    {
        return this.F_Keyin;
    }
    public String GetF_Stat()
    {
        return this.F_Stat;
    }
    public String GetF_Job_Name()
    {
        return this.F_Job_Name;
    }
    public String GetF_Job_Content()
    {
        return this.F_Job_Content;
    }
    public String GetF_Job_InterView()
    {
        return this.F_Job_InterView;
    }
    public String GetF_Job_gender()
    {
        return this.F_Job_gender;
    }
    public String GetF_Job_Level_Base()
    {
        return this.F_Job_Level_Base;
    }
    public String GetF_Job_Level_Top()
    {
        return this.F_Job_Level_Top;
    }
    public String GetF_Job_Age_Base()
    {
        return this.F_Job_Age_Base;
    }
    public String GetF_Job_Age_Top()
    {
        return this.F_Job_Age_Top;
    }
    public String GetF_Job_People()
    {
        return this.F_Job_People;
    }
    public String GetF_Status()
    {
        return this.F_Status;
    }
    public String GetDeptName()
    {
        return this.DeptName;
    }
    public String GetF_DeptCode()
    {
        return this.F_DeptCode;
    }

}