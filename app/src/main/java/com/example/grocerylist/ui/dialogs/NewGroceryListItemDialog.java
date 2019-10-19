package com.example.grocerylist.ui.dialogs;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.grocerylist.R;
import com.example.grocerylist.util.MyDateFormat;
import com.example.grocerylist.entities.GroceryList;
import com.google.android.material.snackbar.Snackbar;

import java.util.Arrays;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.grocerylist.util.Constants.DLG_TITLE;
import static com.example.grocerylist.util.Constants.DL_PRIORITY;
import static com.example.grocerylist.util.Constants.GL_DUE_DATE;
import static com.example.grocerylist.util.Constants.GL_NAME;
import static com.example.grocerylist.util.Constants.GL_POSITION;
import static com.example.grocerylist.util.Constants.GROCERY_LIST_ID;

public class NewGroceryListItemDialog extends DialogFragment {

    /**
     * Interface to be implemented in the calling class.
     * Will return the edited or new list
     *  and the edited items's position or -1 if its new
     */
    public interface DialogSubmitListener {
        void onDialogSubmit(GroceryList groceryList, int position);
    }

    @Nullable @BindView(R.id.tv_dialog_List_name) EditText mGLName;
    @Nullable @BindView(R.id.tv_dialog_date) EditText mDueDate;
    @Nullable @BindView(R.id.spinner_priority) Spinner mPriority;
    @Nullable @BindView(R.id.btn_gl_dialog_submit) Button mSubmit;

    private DialogSubmitListener callback;

    /**
     * New instance of the class to set up the callback function and arguments
     * @param args the dialog information (title and data of the editable item)
     * @param callback function of the parent
     * @return new instance of the fragment
     */
    public static NewGroceryListItemDialog newInstance(Bundle args, DialogSubmitListener callback){
        NewGroceryListItemDialog frag = new NewGroceryListItemDialog();
        frag.callback = callback;
        frag.setArguments(args);
        return frag;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialog_new_gl,null);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.dialog);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getDialog().setTitle(getArguments().getString(DLG_TITLE, "Dialog"));
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        mGLName.setText(getArguments().getString(GL_NAME, ""));
        mDueDate.setText(getArguments().getString(GL_DUE_DATE, ""));
        int priority = getArguments().getInt(DL_PRIORITY,0);
        mPriority.setSelection(priority);
        if(getArguments().containsKey(GL_POSITION)){
            mSubmit.setText("Edit");
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        //getDialog().getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Nullable @OnClick(R.id.btn_gl_dialog_submit)
    public void onAddNewGLItem(){
        String listName = mGLName.getText().toString();
        String dueDate = mDueDate.getText().toString();
        String[] priorities = getResources().getStringArray(R.array.gl_priority);
        String priorityString = mPriority.getSelectedItem().toString();
        int idx = 0;
        if(priorities != null) {
            idx = Arrays.asList(priorities).indexOf(priorityString);
            // in case index was not found
            idx = idx == -1 ? 0 : idx;
        }
        if(listName.trim().isEmpty()){
            Snackbar.make(getView(), "List title cannot be empty", Snackbar.LENGTH_LONG).show();
        } else if(dueDate.trim().isEmpty()){
            Snackbar.make(getView(), "Due date is not set!", Snackbar.LENGTH_LONG).show();
        } else {
            DialogSubmitListener listener = (DialogSubmitListener) getTargetFragment();
            GroceryList groceryList = new GroceryList(listName, dueDate, idx);
            if(getArguments().containsKey(GROCERY_LIST_ID)) {
                groceryList.setId(getArguments().getString(GROCERY_LIST_ID));
            }
            listener.onDialogSubmit(groceryList, getArguments().getInt(GL_POSITION,-1));
            dismiss();
        }
    }

    @Nullable @OnClick(R.id.btn_dlg_cancel)
    public void onCancelDialog(){
        dismiss();
    }

    @Nullable @OnClick(R.id.tv_dialog_date)
    public void onOpenCalendar(){
        Calendar calendar = MyDateFormat.getDateNow(Calendar.getInstance().getTime());
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        if(getArguments() != null && getArguments().containsKey(GL_DUE_DATE)){
            Calendar dueDate = MyDateFormat.getDate(getArguments().getString(GL_DUE_DATE));
            day = dueDate.get(Calendar.DAY_OF_MONTH);
            month = dueDate.get(Calendar.MONTH);
            year = dueDate.get(Calendar.YEAR);
        }

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), R.style.datepicker, (view, year1, monthOfYear, dayOfMonth) -> {
            Calendar d = Calendar.getInstance();
            d.set(year1,monthOfYear,dayOfMonth);
            mDueDate.setText(MyDateFormat.getDate(d));
        }, year, month, day);
        datePickerDialog.show();
    }
}
