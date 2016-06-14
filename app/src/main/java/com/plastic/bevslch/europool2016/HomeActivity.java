package com.plastic.bevslch.europool2016;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.plastic.bevslch.europool2016.Adapters.CircleTransformation;
import com.plastic.bevslch.europool2016.Fragments.CupFragment;
import com.plastic.bevslch.europool2016.Fragments.StandingFragment;
import com.plastic.bevslch.europool2016.Helpers.PreffHelper;
import com.plastic.bevslch.europool2016.Models.Players;
import com.plastic.bevslch.europool2016.bus.EventBus;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle("");
            Picasso.with(this)
                    .load(PreffHelper.getInstance().getPhotoUrl())
                    .error(R.drawable.ic_account)
                    .placeholder(R.drawable.ic_account)
                    .resize(150, 150)
                    .transform(new CircleTransformation())
                    .into((ImageView) toolbar.findViewById(R.id.toolbar_image));
            String name = PreffHelper.getInstance().getName();
            if (name != null && name.contains(" ") && name.split(" ").length == 2) {
                String[] nameParts = name.split(" ");
                ((TextView) toolbar.findViewById(R.id.toolbar_firstname)).setText(nameParts[0]);
                ((TextView) toolbar.findViewById(R.id.toolbar_lastname)).setText(nameParts[1]);
                EventBus.getInstance().addListener(Players.class, new EventBus.BusEventListener<Players>() {
                    @Override
                    public void onBusEvent(Players eventData, Object sender) {
                        if (eventData != null && eventData.getPicUrl().equals(PreffHelper.getInstance().getPhotoUrl())) {
                            ((TextView) toolbar.findViewById(R.id.toolbar_points)).setText(getString(R.string.standing_item_pts, eventData.getPoints()));
                        }
                    }
                });
            } else {
                ((TextView) toolbar.findViewById(R.id.toolbar_firstname)).setText(name);
            }
            setSupportActionBar(toolbar);
        }

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        final ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new CupFragment(), "Cup");
        adapter.addFragment(new StandingFragment(), "Standings");
        viewPager.setAdapter(adapter);
        setTitle(adapter.mFragmentTitleList.get(0));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            new AlertDialog.Builder(this)
                    .setTitle("Logout")
                    .setMessage("Are you sure you want to logout?")
                    .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            PreffHelper.getInstance().clearData();

                            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                            finish();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                    .show();

            return true;
        }
        if(id == R.id.action_help)
        {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Help (Logged in as " + PreffHelper.getInstance().getEmail() + ")");

            WebView wv = new WebView(this);
            wv.loadUrl("http://104.131.118.14/info");
            wv.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);

                    return true;
                }
            });

            alert.setView(wv);
            alert.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });
            alert.show();

        }

        return super.onOptionsItemSelected(item);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
