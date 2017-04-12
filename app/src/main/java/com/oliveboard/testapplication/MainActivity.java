package com.oliveboard.testapplication;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.oliveboard.testapplication.util.ConnectionUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "TestActivity";
    private static final String COURSE_URL = "http://android.oliveboard.in/hiring/mocks.cgi";
    private ProgressDialog mDialog;
    private ArrayList<String> mCourse;
    private ArrayList<String> mDescription;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mCourse = new ArrayList<String>();
        mDescription = new ArrayList<String>();

        //Executing the AsynTask to fetch data.
        new GetCourse().execute();
    }

    private void createTabView(ArrayList<String> courses) {
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);

        for (String tabname : courses) {
            tabLayout.addTab(tabLayout.newTab().setText(tabname));
        }

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());

        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    /**
     * Pager adapter dynamically creating fragment.
     */
    public class PagerAdapter extends FragmentStatePagerAdapter {
        int numOfTabs;

        public PagerAdapter(FragmentManager fragmentManager, int NumOfTabs) {
            super(fragmentManager);
            this.numOfTabs = NumOfTabs;
        }

        @Override
        public Fragment getItem(int position) {
            ModelFragment fragment = new ModelFragment();
            //Setting the description of the course from description arraylist.
            fragment.setDescriptionText(mDescription.get(position));
            return fragment;
        }

        @Override
        public int getCount() {
            return numOfTabs;
        }
    }

    /**
     * AysnTask to fetch the content from server in background.
     */
    private class GetCourse extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Showing the proress dialog untill data is fetched.
            mDialog = new ProgressDialog(MainActivity.this);
            mDialog.setMessage(getString(R.string.msg_loading_content));
            mDialog.setCancelable(false);
            mDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Sending request to the server and getting mCourse data as String.
            String jsonStr = ConnectionUtil.getDataFromServer(COURSE_URL);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONArray exams = jsonObj.getJSONArray("exams");

                    for (int i = 0; i < exams.length(); i++) {
                        JSONArray innerCourseArray = exams.getJSONArray(i);
                        mCourse.add(innerCourseArray.getString(0));
                        mDescription.add(innerCourseArray.getString(1));
                    }
                    Log.d(TAG, "Courses : " + mCourse);
                    Log.d(TAG, "Course Description : " + mDescription);
                } catch (JSONException e) {
                    Log.e(TAG, "JSONException" + e.getMessage());
                    e.printStackTrace();
                }

            } else {
                Log.e(TAG, "Couldn't get response from the server.");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (mDialog.isShowing()) {
                mDialog.dismiss();
            }
            //creating tab view.
            createTabView(mCourse);
        }
    }
}
