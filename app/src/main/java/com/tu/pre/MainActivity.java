package com.tu.pre;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.gson.GsonBuilder;
import com.tu.pre.model.Address;
import com.tu.pre.model.User;
import com.tu.preferences.PreferenceUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.save).setOnClickListener(this);
        findViewById(R.id.get).setOnClickListener(this);
        findViewById(R.id.clean).setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.save:
                User user = new User();
                Map<Double,Double> point =new HashMap<>(1);
                point.put(29.35,106.33);
                user.address = new Address("重庆市",point);
                user.birthday=new Date();
                user.id=1;
                user.name="Tu";

                user.tags=new ArrayList<String>();
                user.tags.add("Android");
                user.tags.add("程序猿");

                PreferenceUtils.setObject(this, user);
                break;
            case R.id.get:
                Toast.makeText(this, new GsonBuilder().create().toJson(PreferenceUtils.getObject(this,User.class)),Toast.LENGTH_LONG).show();
                break;
            case R.id.clean:
                PreferenceUtils.clearObject(this,User.class);
                break;
        }
    }
}
