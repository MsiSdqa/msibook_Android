package dqa.com.msibook;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

public class msibook_mmc_detail extends AppCompatActivity {

    String AssetSN;
    String HPAssetSN;
    String Class;
    String BrandName;
    String TypeName;
    String CNAME;
    String CrDate;
    String EMail;
    String Description;
    String Vendor_PN;
    String PartNO;

    TextView txt_AssetSN;
    TextView txt_HPAssetSN;
    TextView txt_Class;
    TextView txt_BrandName;
    TextView txt_TypeName;
    TextView txt_CNAME;
    TextView txt_CrDate;
    TextView txt_EMail;
    TextView txt_Description;
    TextView txt_Vendor_PN;
    TextView txt_PartNO;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msibook_mmc_detail);

        txt_AssetSN = (TextView) findViewById(R.id.txt_AssetSN);
        txt_HPAssetSN = (TextView) findViewById(R.id.txt_HPAssetSN);
        txt_Class = (TextView) findViewById(R.id.txt_Class);
        txt_BrandName = (TextView) findViewById(R.id.txt_BrandName);
        txt_TypeName = (TextView) findViewById(R.id.txt_TypeName);
        txt_CNAME = (TextView) findViewById(R.id.txt_CNAME);
        txt_CrDate = (TextView) findViewById(R.id.txt_CrDate);
        txt_EMail = (TextView) findViewById(R.id.txt_EMail);
        txt_Description = (TextView) findViewById(R.id.txt_Description);
        txt_Vendor_PN = (TextView) findViewById(R.id.txt_Vendor_PN);
        txt_PartNO = (TextView) findViewById(R.id.txt_PartNO);

        AssetSN = getIntent().getStringExtra("AssetSN");
        HPAssetSN = getIntent().getStringExtra("HPAssetSN");
        Class = getIntent().getStringExtra("Class");
        BrandName = getIntent().getStringExtra("BrandName");
        TypeName = getIntent().getStringExtra("TypeName");
        CNAME = getIntent().getStringExtra("CNAME");
        CrDate = getIntent().getStringExtra("CrDate");
        EMail = getIntent().getStringExtra("EMail");
        Description = getIntent().getStringExtra("Description");
        Vendor_PN = getIntent().getStringExtra("Vendor_PN");
        PartNO = getIntent().getStringExtra("PartNO");



        txt_AssetSN.setText(AssetSN);
        txt_HPAssetSN.setText(HPAssetSN);
        txt_Class.setText(Class);
        txt_BrandName.setText(BrandName);
        txt_TypeName.setText(TypeName);
        txt_CNAME.setText(CNAME);
        txt_CrDate.setText(CrDate);
        txt_EMail.setText(EMail);
        txt_Description.setText(Description);
        txt_Vendor_PN.setText(Vendor_PN);
        txt_PartNO.setText(PartNO);

    }
}
