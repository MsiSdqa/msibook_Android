package dqa.com.msibook;

import java.io.Serializable;

public class msibook_ehr_item implements Serializable {
    String F_Master_ID;
    String F_DeptCode;
    String F_Job_Dept;
    String F_Job_vacancies;
    String F_Job_People;
    String Member_JobCount;

    public msibook_ehr_item(String F_Master_ID,String F_DeptCode,String F_Job_Dept,String F_Job_vacancies,String F_Job_People,String Member_JobCount) {

        this.F_Master_ID = F_Master_ID;
        this.F_DeptCode = F_DeptCode;
        this.F_Job_Dept = F_Job_Dept;
        this.F_Job_vacancies = F_Job_vacancies;
        this.F_Job_People = F_Job_People;
        this.Member_JobCount = Member_JobCount;
    }

}
