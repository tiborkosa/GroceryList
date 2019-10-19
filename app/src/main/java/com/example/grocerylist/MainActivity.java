package com.example.grocerylist;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import com.example.grocerylist.util.UserUtil;
import com.example.grocerylist.entities.User;
import com.example.grocerylist.ui.grocerylist.GroceryListFragment;
import com.example.grocerylist.ui.user.UserViewModel;
import com.firebase.ui.auth.AuthUI;

import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import timber.log.Timber;

import static com.example.grocerylist.ui.grocerylist.GroceryListViewModel.listRef;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;
    private FirebaseDatabase mFirebaseDatabase;
    private Boolean isTablet = false;

    private static final int RC_SIGN_IN = 7979;
    private FirebaseAuth mFirebaseAuth;
    private UserViewModel userViewModel;
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        isTablet = getResources().getBoolean(R.bool.tablet);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        if(isTablet){
            mAppBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.nav_settings,
                    R.id.nav_user_profile, R.id.nav_share, R.id.nav_messages, R.id.nav_list)
                    .setDrawerLayout(drawer)
                    .build();

            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction transaction = fm.beginTransaction();
            transaction.add(R.id.nav_host_fragment_gl, GroceryListFragment.newInstance(), "gl_fragment");
            transaction.commit();
        } else {
            mAppBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.nav_home, R.id.nav_settings,
                    R.id.nav_user_profile, R.id.nav_share, R.id.nav_messages, R.id.nav_list)
                    .setDrawerLayout(drawer)
                    .build();
        }
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.setNavigationItemSelectedListener(this);

        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        userViewModel.getUser().observe(this, user -> {
            Timber.d("Logged user is:" + user.toString());
            updateNavigationUser(user);
        });

    }

    private void updateNavigationUser(User user) {
        NavigationView navigation = findViewById(R.id.nav_view);
        View headerView = navigation.getHeaderView(0);
        TextView mNavUserName = headerView.findViewById(R.id.nav_header_name);
        TextView mNavUserEmail = headerView.findViewById(R.id.tv_header_email);
        ImageView mProfileImage = headerView.findViewById(R.id.iv_header_img);
        if(user.getImage() != null){
            Picasso.get().load(user.getImage()).into(new Target(){

                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    mProfileImage.setImageBitmap(bitmap);
                }

                @Override
                public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(final Drawable placeHolderDrawable) {
                    Timber.d("Prepare Load");
                }
            });
        }
        if(user.getEmail() != null){
            mNavUserEmail.setText(user.getEmail());
        }
        mNavUserName.setText(user.getName());

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Timber.d( "this is the requestCode: " + requestCode);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if(resultCode == RESULT_OK && requestCode == RC_SIGN_IN){

            User oldUser = UserUtil.getUser();
            if(!firebaseUser.getUid().equals(oldUser.getId())) {
                copyExistingData(oldUser.getId(), firebaseUser.getUid());

                DatabaseReference myRef = mFirebaseDatabase.getReference("users/" + firebaseUser.getUid());
                String name = firebaseUser.getDisplayName();
                String email = firebaseUser.getEmail();
                Uri photoUri = firebaseUser.getPhotoUrl();
                String photo = null;
                if (photoUri != null)
                    photo = photoUri.toString();
                User user = new User(firebaseUser.getUid(), name, email, photo, false);
                myRef.setValue(user);

                UserUtil.setUser(user);
                userViewModel.setUser(user);
                listRef = FirebaseDatabase.getInstance().getReference(firebaseUser.getUid()+"/my_list");
                Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.nav_home);
            }
            NavigationView navigationView = findViewById(R.id.nav_view);
            Menu menu = navigationView.getMenu();
            MenuItem menuItem = menu.findItem(R.id.nav_authenticate);
            menuItem.setTitle(R.string.menu_logout);
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.nav_home:
                if(isTablet) {
                    Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.nav_list);
                } else {
                    Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.nav_home);
                }
                break;
            case R.id.nav_user_profile:
                Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.nav_user_profile);
                break;
            case R.id.nav_settings:
                Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.nav_settings);
                break;
            case R.id.nav_messages:
                Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.nav_messages);
                break;
            case R.id.nav_authenticate:

                if(mFirebaseAuth.getCurrentUser() == null){
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setAvailableProviders(Arrays.asList(
                                            new AuthUI.IdpConfig.GoogleBuilder().build(),
                                            new AuthUI.IdpConfig.FacebookBuilder().build(),
                                            new AuthUI.IdpConfig.EmailBuilder().build()))
                                    .setIsSmartLockEnabled(false)
                                    //.setTheme(R.style.LogInTheme)
                                    .build(),
                            RC_SIGN_IN);
                }else {
                    menuItem.setTitle(R.string.menu_login);
                    mFirebaseAuth.signOut();
                }
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void copyExistingData(String userIdFrom, final String userIdTo){
        DatabaseReference dReferenceFrom = mFirebaseDatabase.getReference(userIdFrom+ "/my_list");

        dReferenceFrom.addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DatabaseReference dReferenceTo = mFirebaseDatabase.getReference(userIdTo+"/my_list");
                Map<String, Object> map = new HashMap<>();
                for (DataSnapshot list: dataSnapshot.getChildren()){
                    map.put(list.getKey(), list.getValue());
                }
                dReferenceTo.updateChildren(map);

                // delete old data
                dReferenceFrom.removeValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Timber.d("Could not get old data.");
            }
        });
    }
}
