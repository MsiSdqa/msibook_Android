package dqa.com.msibook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class msibook_mmc_barcode_search extends AppCompatActivity {

    private IntentIntegrator scanIntegrator;

    public void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null)
        {
            if(scanningResult.getContents() != null)
            {
                String scanContent = scanningResult.getContents();
                if (!scanContent.equals(""))
                {
                    Toast.makeText(getApplicationContext(),"掃描產編: "+scanContent.toString(), Toast.LENGTH_LONG).show();

                    Intent intent1 = new Intent();

                    intent1.putExtra("Type","1");   //

                    intent1.putExtra("txt_Edit",scanContent.toString());

                    intent1.setClass(msibook_mmc_barcode_search.this, msibook_mmc_detail.class);
                    //開啟Activity
                    startActivity(intent1);

                    finish();
                }
            }
        }
        else
        {
            super.onActivityResult(requestCode, resultCode, intent);
            Toast.makeText(getApplicationContext(),"發生錯誤",Toast.LENGTH_LONG).show();


        }


        if(intent ==null) {
            finish();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msibook_mmc_barcode_search);

        View txt_barcode = (View) findViewById(R.id.txt_barcode);

        scanIntegrator = new IntentIntegrator(msibook_mmc_barcode_search.this);
        scanIntegrator.setOrientationLocked(false);    // 加入這一行指令
        scanIntegrator.setPrompt("掃描產編");
        scanIntegrator.setTimeout(300000);
        scanIntegrator.initiateScan();
    }

}
