package com.plastic.bevslch.europool2016;

import android.animation.ValueAnimator;
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
import android.view.View;
import android.view.animation.RotateAnimation;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
            final TextView pointsText = (TextView) toolbar.findViewById(R.id.toolbar_points);
            final ImageView userPic = (ImageView) toolbar.findViewById(R.id.toolbar_image);
            Picasso.with(this)
                    .load(PreffHelper.getInstance().getPhotoUrl())
                    .error(R.drawable.ic_account)
                    .placeholder(R.drawable.ic_account)
                    .resize(150, 150)
                    .transform(new CircleTransformation())
                    .into(userPic);
            userPic.setOnClickListener(new View.OnClickListener() {
                private int tapCount;

                @Override
                public void onClick(View v) {
                    RotateAnimation rotateAnimation = new RotateAnimation(0, 360, v.getWidth() / 2, v.getHeight() / 2);
                    rotateAnimation.setDuration(500);
                    v.startAnimation(rotateAnimation);
                    tapCount++;
                    if (tapCount == 1) {
                        Toast.makeText(getBaseContext(), "Hi!" + new String(Character.toChars(0x1F60A)), Toast.LENGTH_LONG).show();
                    } else if (tapCount == 5) {
                        Toast.makeText(getBaseContext(), "Its getting annoying!" + new String(Character.toChars(0x1F620)), Toast.LENGTH_LONG).show();
                    } else if (tapCount == 15) {
                        Toast.makeText(getBaseContext(), "Stop it!" + new String(Character.toChars(0x1F624)), Toast.LENGTH_LONG).show();
                    } else if (tapCount == 20) {
                        Toast.makeText(getBaseContext(), "Last warning!!!" + new String(Character.toChars(0x1F621)), Toast.LENGTH_LONG).show();
                    } else if (tapCount == 30) {
                        Toast.makeText(getBaseContext(), "You just had to do it!" + new String(Character.toChars(0x1F625)), Toast.LENGTH_LONG).show();
                        RotateAnimation rotateToolbarAnimation = new RotateAnimation(0, 3600, toolbar.getWidth() / 2, toolbar.getHeight() / 2);
                        rotateToolbarAnimation.setDuration(5000);
                        toolbar.startAnimation(rotateToolbarAnimation);
                        RotateAnimation rotateTabsAnimation = new RotateAnimation(0, 3600, tabLayout.getWidth() / 2, tabLayout.getHeight() / 2);
                        rotateTabsAnimation.setDuration(5000);
                        tabLayout.startAnimation(rotateTabsAnimation);
                        RotateAnimation rotatePagerAnimation = new RotateAnimation(0, 3600, viewPager.getWidth() / 2, viewPager.getHeight() / 2);
                        rotatePagerAnimation.setDuration(5000);
                        viewPager.startAnimation(rotatePagerAnimation);
                    }
                }
            });
            String name = PreffHelper.getInstance().getName();
            if (name != null && name.contains(" ") && name.split(" ").length == 2) {
                String[] nameParts = name.split(" ");
                ((TextView) toolbar.findViewById(R.id.toolbar_firstname)).setText(nameParts[0]);
                ((TextView) toolbar.findViewById(R.id.toolbar_lastname)).setText(nameParts[1]);
                EventBus.getInstance().addListener(Players.class, new EventBus.BusEventListener<Players>() {
                    @Override
                    public void onBusEvent(Players eventData, Object sender) {
                        if (eventData != null && eventData.getPicUrl().equals(PreffHelper.getInstance().getPhotoUrl())) {
                            pointsText.setText(getString(R.string.standing_item_pts, eventData.getPoints()));
                            ValueAnimator pointAnimation = ValueAnimator.ofInt(0, eventData.getPoints())
                                    .setDuration(1000);
                            pointAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                @Override
                                public void onAnimationUpdate(ValueAnimator animation) {
                                    int value = (Integer) animation.getAnimatedValue();
                                    pointsText.setText(getString(R.string.standing_item_pts, value));
                                }
                            });
                            pointAnimation.start();
                            RotateAnimation rotateAnimation = new RotateAnimation(0, 360, userPic.getWidth() / 2, userPic.getHeight() / 2);
                            rotateAnimation.setDuration(1000);
                            userPic.startAnimation(rotateAnimation);
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
            alert.setTitle("Help");

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
