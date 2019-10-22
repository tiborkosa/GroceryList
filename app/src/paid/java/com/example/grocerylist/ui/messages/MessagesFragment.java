package com.example.grocerylist.ui.messages;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.grocerylist.R;

/**
 * Messages fragment is used to gl users to communicate
 * by sending messages
 */
public class MessagesFragment extends Fragment {

    private MessagesViewModel messagesViewModel;

    /**
     * onCreateView to inflate the view and add the observer
     * @param inflater layout inflater
     * @param container where we want to inflate the layout
     * @param savedInstanceState
     * @return inflated view
     */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        messagesViewModel =
                ViewModelProviders.of(this).get(MessagesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_messages, container, false);
        final TextView textView2 = root.findViewById(R.id.text_messages);
        messagesViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView2.setText(s);
            }
        });
        return root;
    }
}