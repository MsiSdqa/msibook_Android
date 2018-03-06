package dqa.com.msibook;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;//
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

public class MainPage extends AppCompatActivity



        implements NavigationView.OnNavigationItemSelectedListener {

    private Context mContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mContent = this;
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Button Btn_RSS = (Button)findViewById(R.id.Btn_RSS);

        Btn_RSS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent RSS_Intent = new Intent(mContent,msibook_rss.class);

                mContent.startActivity(RSS_Intent);
            }
        });

        Button Btn_IMS = (Button)findViewById(R.id.Btn_IMS);

        Btn_IMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent msibook_ims_myissue = new Intent(mContent,msibook_ims_issue_myissue.class);

                mContent.startActivity(msibook_ims_myissue);
            }
        });

        Button Btn_MMC = (Button)findViewById(R.id.Btn_MMC);

        Btn_MMC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent msibook_mmc = new Intent(mContent,msibook_mmc.class);

                mContent.startActivity(msibook_mmc);
            }
        });


        Button Btn_Resume = (Button)findViewById(R.id.Btn_Resume);

        Btn_Resume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent msibook_resume = new Intent(mContent,msibook_resume.class);

                mContent.startActivity(msibook_resume);
            }
        });

        Button Btn_Lab = (Button)findViewById(R.id.Btn_Lab);

        Btn_Lab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Btn_Lab = new Intent(mContent,msibook_facility.class);

                mContent.startActivity(Btn_Lab);
            }
        });

        Button Btn_dqaweekly = (Button)findViewById(R.id.Btn_dqaweekly);

        Btn_dqaweekly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent dqaweekly = new Intent(mContent,msibook_dqaweekly_main.class);

                mContent.startActivity(dqaweekly);
            }
        });

        Button Btn_eHR = (Button)findViewById(R.id.Btn_eHR);

        Btn_eHR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent dqaweekly = new Intent(mContent,msibook_ehr_splashscreen.class);

                mContent.startActivity(dqaweekly);
            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }





    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_msi_point) {
            Intent RSS_Intent = new Intent(mContent,msibook_store.class);

            mContent.startActivity(RSS_Intent);
        } else if (id == R.id.nav_ims) {

        } else if (id == R.id.nav_overtime) {

        } else if (id == R.id.nav_weekly_report) {

        } else if (id == R.id.nav_resource_share) {

        } else if (id == R.id.nav_resume) {

        } else if (id == R.id.nav_mmc) {

        } else if (id == R.id.nav_laboratory) {

        }else if (id == R.id.nav_message) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
