package com.example.grocerylist.ui.dialogs;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.grocerylist.R;
import com.example.grocerylist.Util.MyDateFormat;
import com.example.grocerylist.entities.GroceryList;
import com.google.android.material.snackbar.Snackbar;

import java.util.Arrays;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.grocerylist.Util.Constants.DLG_TITLE;
import static com.example.grocerylist.Util.Constants.DL_IS_EDITED;
import static com.example.grocerylist.Util.Constants.DL_PRIORITY;
import static com.example.grocerylist.Util.Constants.GL_DUE_DATE;
import static com.example.grocerylist.Util.Constants.GL_NAME;
import static com.example.grocerylist.Util.Constants.GROCERY_LIST_ID;

public class NewGroceryListItemDialog extends DialogFragment {

    public interface DialogSubmitListener {
        void onDialogSubmit(GroceryList groceryList, boolean isEdited);
    }

    @Nullable @BindView(R.id.tv_dialog_List_name) EditText mGLname;
    @Nullable @BindView(R.id.tv_dialog_date) EditText mDueDate;
    @Nullable @BindView(R.id.ib_calendar) ImageButton mCalendar;
    @Nullable @BindView(R.id.spinner_priority) Spinner mPriority;
    @Nullable @BindView(R.id.btn_dlg_cancel) Button mCancel;
    @Nullable @BindView(R.id.btn_gl_dialog_submit) Button mSubmit;

    private DialogSubmitListener callback;

    public static NewGroceryListItemDialog newInstance(String title, DialogSubmitListener callback){
        NewGroceryListItemDialog frag = new NewGroceryListItemDialog();
        frag.callback = callback;
        Bundle args = new Bundle();
        args.putString(DLG_TITLE, title);
        frag.setArguments(args);
        return frag;
    }

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
        if(callback == null || !(callback instanceof DialogSubmitListener)){
            throw new UnsupportedOperationException("DialogSubmitListener.onDialogSubmit is not implemented");
        }
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getDialog().setTitle(getArguments().getString(DLG_TITLE, "Dialog"));
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        mGLname.setText(getArguments().getString(GL_NAME, ""));
        mDueDate.setText(getArguments().getString(GL_DUE_DATE, ""));
        int priority = getArguments().getInt(DL_PRIORITY,0);
        mPriority.setSelection(priority);
    }

    @Nullable @OnClick(R.id.btn_gl_dialog_submit)
    public void onAddNewGLItem(){
        boolean isEdited = getArguments().getBoolean(DL_IS_EDITED, false);
        String listName = mGLname.getText().toString();
        String dueDate = mDueDate.getText().toString();
        String[] priorities = getResources().getStringArray(R.array.gl_priority);
        String priorityString = mPriority.getSelectedItem().toString();
        int idx = 0;
        if(priorities != null) {
            idx = Arrays.asList(priorities).indexOf(priorityString);
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
            listener.onDialogSubmit(groceryList, isEdited);
            dismiss();
        }
    }

    @Nullable @OnClick(R.id.btn_dlg_cancel)
    public void onCancelDialog(){
        dismiss();
    }

    @Nullable @OnClick(R.id.ib_calendar)
    public void onOpenCalendar(){
        Calendar calendar = MyDateFormat.getDateNow(Calendar.getInstance().getTime());
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        if(getArguments() != null && getArguments().containsKey(GL_DUE_DATE)){
            // TODO: split up the string or whatever to show correct date
            Calendar dueDate = MyDateFormat.getDate(getArguments().getString(GL_DUE_DATE));
            day = dueDate.get(Calendar.DAY_OF_MONTH);
            month = dueDate.get(Calendar.MONTH);
            year = dueDate.get(Calendar.YEAR);
        }

        Log.d("DATA_DATE", "day: " + day +" month: " + month + " year: " + year);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), R.style.datepicker, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar d = Calendar.getInstance();
                d.set(year,monthOfYear,dayOfMonth);
                mDueDate.setText(MyDateFormat.getDate(d));
            }
        }, year, month, day);
        datePickerDialog.show();
    }
}
