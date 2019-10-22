package com.example.grocerylist.ui.share;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grocerylist.adapters.ShareEmailRecycleViewAdapter;
import com.example.grocerylist.R;
import com.example.grocerylist.util.EmailUtil;
import com.example.grocerylist.util.MyApplication;
import com.example.grocerylist.entities.GroceryList;
import com.example.grocerylist.entities.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

import static com.example.grocerylist.util.Constants.GL_CREATE_DATE;
import static com.example.grocerylist.util.Constants.GL_DUE_DATE;
import static com.example.grocerylist.util.Constants.GL_NAME;
import static com.example.grocerylist.util.Constants.GROCERY_LIST_ID;

/**
 * Sharing the list
 * for the free version we only share it via email
 */
public class ShareFragment extends Fragment implements ShareEmailRecycleViewAdapter.OnItemClicked {

    public static final String EMAILS = "emails";
    @BindView(R.id.et_email_search) EditText mEmailSearch;
    @BindView(R.id.ib_share_list) ImageButton mShareBtn;
    @BindView(R.id.tv_gli_name) TextView mShareListName;
    @BindView(R.id.tv_gli_due_date_text) TextView mShareListDueDate;
    @BindView(R.id.rv_share_email_list) RecyclerView mRecyclerView;

    private ArrayList<String> shareEmailList = new ArrayList<>();
    private List<User> shareGLUserList = new ArrayList<>();
    private ShareEmailRecycleViewAdapter adapter;
    private GroceryList mGroceryList;

    private static final String TAG = ShareFragment.class.getSimpleName();

    /**
     * Getting a new instance of the fragment
     * @param args
     * @return
     */
    public static ShareFragment newInstance(Bundle args){
        ShareFragment frag = new ShareFragment();
        frag.setArguments(args);
        return frag;
    }

    /**
     * on view created we set up the needed fields
     * @param view inflated view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mGroceryList = new GroceryList();
        mGroceryList.setDueDate(getArguments().get(GL_DUE_DATE).toString());
        mGroceryList.setId(getArguments().get(GROCERY_LIST_ID).toString());
        mGroceryList.setListName(getArguments().get(GL_NAME).toString());
        mGroceryList.setCreateDate(getArguments().get(GL_CREATE_DATE).toString());

        if(mGroceryList.getDueDate() != null)
            mShareListDueDate.setText(mGroceryList.getDueDate());
        if(mGroceryList.getListName() != null)
            mShareListName.setText(mGroceryList.getListName());

        adapter = new ShareEmailRecycleViewAdapter(shareEmailList, this);
        mRecyclerView.setAdapter(adapter);
    }

    /**
     * on create view we inflate the layout
     * @param inflater layout inflater
     * @param container where we want to inflate the fragment
     * @param savedInstanceState
     * @return inflated view
     */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_share, container, false);
        ButterKnife.bind(this, root);
        setRetainInstance(true);
        mShareBtn.setVisibility(View.GONE);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));

        return root;
    }

    /**
     * overridden interface of the @ShareEmailRecycleViewAdapter
     * @param position of the item to be deleted
     */
    @Override
    public void onDeleteItem(int position) {
        shareEmailList.remove(position);
        if(shareGLUserList.size() > position){
            shareGLUserList.remove(position);
        }
        updateRecyclerView();
    }

    /**
     * Adding new email to the list
     * We check if it is a valid email
     */
    @OnClick(R.id.btn_add_email)
    public void onAddEmailClicked(){
        InputMethodManager inputManager = (InputMethodManager) this.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(this.getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        String email = mEmailSearch.getText().toString();

        if(isValidEmail(email)){
            shareEmailList.add(email);
            mEmailSearch.setText("");
            updateRecyclerView();
        } else {
            Toast.makeText(getContext(), "Entered email is invalid!", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Helper function to update the view
     */
    private void updateRecyclerView(){
        Timber.d( "updateRecyclerView");
        adapter = new ShareEmailRecycleViewAdapter(shareEmailList, this);
        mRecyclerView.setAdapter(adapter);
    }

    /**
     * Checking if the entered email is valid or not
     * @param email to be validated
     * @return true if valid, false if not
     */
    private boolean isValidEmail(String email) {
        String regex = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
        return email.matches(regex);
    }

    /**
     * Sharing the list
     * We are using the installed email apps
     */
    @OnClick(R.id.btn_share_list)
    public void validateAndShareList(){
        if(shareEmailList.size() == 0){
            Toast.makeText(getContext(), "Add emails first!", Toast.LENGTH_LONG).show();
            return;
        }

        String[] arrEmailsTo = new String[shareEmailList.size()];
        FirebaseDatabase.getInstance().getReference("/g_list_items/"+mGroceryList.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot == null || dataSnapshot.equals("")) {
                    Toast.makeText(getContext(),
                            "You must have items in the list", Toast.LENGTH_SHORT).show();
                } else {
                    String body = EmailUtil.buildContent(mGroceryList, dataSnapshot);
                    sentEmail(body, arrEmailsTo);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MyApplication.getAppContext(),
                        "Could not send email.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Sending the email
     * @param body of the email
     * @param emailToList the list of users we want to share the list with
     */
    private void sentEmail(String body, String[] emailToList){
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Grocery List");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, emailToList);
        emailIntent.putExtra(Intent.EXTRA_TEXT, body);

        try {
            startActivity(emailIntent);
            Timber.i("Email sent!");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(MyApplication.getAppContext(),
                    "Email not installed.", Toast.LENGTH_SHORT).show();
            Timber.d( ex.getMessage());
        }
    }

    /**
     * going back to the previous page
     */
    @OnClick(R.id.btn_cancel_share)
    public void goBack(){
        this.getFragmentManager().popBackStack();
    }

}