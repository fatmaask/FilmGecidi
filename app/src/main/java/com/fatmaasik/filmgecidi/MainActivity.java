package com.fatmaasik.filmgecidi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    String fragment_name = "";
    DrawerLayout drawer;
    SessionManager session;
    // Button Logout
    Button logoutButton;
    Button loginButton;
    ImageView profilImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        displaySelectedScreen(R.id.nav_home, "menu");

        View headerView = navigationView.getHeaderView(0);
        loginButton = (Button) headerView.findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displaySelectedScreen(R.id.loginButton, "menu");

            }
        });

        session = new SessionManager(getApplicationContext());
        // Button logout
        logoutButton = (Button) headerView.findViewById(R.id.logout);

        logoutButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Clear the session data
                // This will clear all session data and
                // redirect user to LoginActivity
                session.logoutUser();

            }
        });

        TextView lblName = (TextView) headerView.findViewById(R.id.loginTitle);
        TextView lblEmail = (TextView) headerView.findViewById(R.id.loginEmail);
        ImageView profileImage = (ImageView) headerView.findViewById(R.id.loginImage);
        if(session.isLoggedIn()){
            HashMap<String, String> user = session.getUserDetails();
            lblName.setVisibility(View.VISIBLE);
            lblEmail.setVisibility(View.VISIBLE);
            lblName.setText(user.get(SessionManager.KEY_NAME));
            lblEmail.setText(user.get(SessionManager.KEY_EMAIL));
            profileImage.setImageResource(R.mipmap.ic_profile);
            Picasso.with(getApplication()).load("http://fatmaasik.hol.es/public/static/photos/"+user.get(SessionManager.KEY_IMAGE)).into(profileImage);
            logoutButton.setVisibility(View.VISIBLE);
            loginButton.setVisibility(View.GONE);
            profilImage = (ImageView) headerView.findViewById(R.id.loginImage);
            profilImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    displaySelectedScreen(R.id.loginImage, null);
                }
            });
        } else if (!session.isLoggedIn()){
            lblName.setVisibility(View.GONE);
            lblEmail.setVisibility(View.GONE);
            logoutButton.setVisibility(View.GONE);
            loginButton.setVisibility(View.VISIBLE);
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void displaySelectedScreen(int itemId, String movieId) {

        //creating fragment object
        Fragment fragment = null;


        //initializing the fragment object which is selected
        switch (itemId) {
            case R.id.nav_home:
                fragment = new HomeFragment();
                fragment_name = "Home";
                break;
            case R.id.nav_movies:
                fragment = new MoviesFragment();
                fragment_name = "Movies";
                break;
            case R.id.nav_top_movies:
                fragment = new TopFragment();
                fragment_name = "Top";
                break;
            case 4:
                fragment = new MovieDetail();
                Bundle args = new Bundle();
                args.putString("movie_id", movieId);
                fragment.setArguments(args);
                fragment_name = "Movie_" + movieId;
                break;
            case R.id.loginButton:
                fragment = new LoginFragment();
                fragment_name = "Login";
                break;
            case R.id.email_register_button:
                fragment = new RegisterFragment();
                fragment_name = "Register";
                break;
            case R.id.loginImage:
                fragment = new ProfileFragment();
                fragment_name = "Profile";
                break;
            case R.id.nav_about:
                fragment = new AboutFragment();
                fragment_name = "About";
                break;
            default:
                break;
        }

        //replacing the fragment
        if(fragment != null){

            // Fragment transaction nesnesi ile fragment ekranları arasında geçiş sağlıyor
            FragmentManager fragmentManager = getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.frame_container, fragment).addToBackStack(fragment_name).commit();

            // Stack te bulunan fragment sayısını alıyor
            int count = getSupportFragmentManager().getBackStackEntryCount();

            if(count!=0) {
                // Son fragment alınıyor
                FragmentManager.BackStackEntry backStackEntry = getSupportFragmentManager().getBackStackEntryAt(count - 1);

                // Son fragment ile seçilen fragment aynı ise eski fragment siliniyor
                if (backStackEntry.getName().contains(fragment_name)) {
                    getSupportFragmentManager().popBackStack();
                }
            }
        }

        drawer.closeDrawer(GravityCompat.START);
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        displaySelectedScreen(item.getItemId(), "menu");
        item.setChecked(true);
        System.out.println("Menu ItemId : " + item.getItemId());
        //make this method blank
        return true;
    }
}
