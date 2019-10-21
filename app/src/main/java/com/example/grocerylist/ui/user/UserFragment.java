package com.example.grocerylist.ui.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.grocerylist.R;
import com.example.grocerylist.entities.User;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * User fragment for the profile
 */
public class UserFragment extends Fragment {

    private UserViewModel userViewModel;
    @BindView(R.id.tv_user_name) TextView mUserName;
    @BindView(R.id.tv_user_email) TextView mUserEmail;
    @BindView(R.id.iv_user_pic) ImageView mUserProfileImage;

    /**
     * inflating the layout
     * @param inflater layout inflater
     * @param container container where we want to show the fragment
     * @param savedInstanceState
     * @return inflated view
     */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        userViewModel =
                ViewModelProviders.of(this).get(UserViewModel.class);
        View root = inflater.inflate(R.layout.fragment_user, container, false);
        ButterKnife.bind(this, root);

        userViewModel.getUser().observe(this, user -> {
            mUserName.setText(user.getName());
            if(user.getEmail() != null)
                mUserEmail.setText(user.getEmail());
        });
        return root;
    }
}