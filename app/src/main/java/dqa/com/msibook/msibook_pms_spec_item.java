package dqa.com.msibook;

import java.io.Serializable;

public class msibook_pms_spec_item implements Serializable {

    String FieldName;
    String SpecData;

    public msibook_pms_spec_item(String FieldName,String SpecData){

        this.FieldName = FieldName;

        this.SpecData = SpecData;

    }

}
