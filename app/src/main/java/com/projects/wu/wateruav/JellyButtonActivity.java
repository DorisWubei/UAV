package com.projects.wu.wateruav;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class JellyButtonActivity extends AppCompatActivity {

    private ActionMenu actionMenuBottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jellybutton);

        actionMenuBottom = (ActionMenu) findViewById(R.id.actionMenuBottom);

        // add menu items

        actionMenuBottom.addView(R.drawable.data_chart, getItemColor(R.color.menuNormalInfo), getItemColor(R.color.menuPressInfo));
        actionMenuBottom.addView(R.drawable.data_list, getItemColor(R.color.menuNormalRed), getItemColor(R.color.menuPressRed));
        actionMenuBottom.addView(R.drawable.about_us);


        actionMenuBottom.setItemClickListener(new OnActionItemClickListener() {
            @Override
            public void onItemClick(int index) {
                switch (index) {
                    case 1: {
                        Intent i = new Intent(JellyButtonActivity.this,DataChartActivity.class);
                        startActivity(i);
                        break;
                    }

                    case 2: {
                        Intent i = new Intent(JellyButtonActivity.this,DatalistActivity.class);
                        startActivity(i);break;
                    }

                    case 3: {
                        Intent i = new Intent(JellyButtonActivity.this,AboutUsActivity.class);
                        startActivity(i);break;
                    }
                }
            }

            @Override
            public void onAnimationEnd(boolean isOpen) {
                //       showMessage("animation end : " + isOpen);
            }
        });
    }


    private int getItemColor(int colorID) {
        return getResources().getColor(colorID);
    }

}
