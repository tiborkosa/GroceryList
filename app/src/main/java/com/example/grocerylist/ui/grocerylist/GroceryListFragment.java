package com.example.grocerylist.ui.grocerylist;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grocerylist.Adapters.GroceryListRecycleViewAdapter;
import com.example.grocerylist.Adapters.MyItemTouchHelper;
import com.example.grocerylist.Adapters.MyTouchListener;
import com.example.grocerylist.R;
import com.example.grocerylist.Util.UserUtil;
import com.example.grocerylist.ui.dialogs.NewGroceryListItemDialog;
import com.example.grocerylist.entities.GroceryList;
import com.example.grocerylist.entities.User;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.grocerylist.Util.Constants.DLG_TITLE;
import static com.example.grocerylist.Util.Constants.DL_IS_EDITED;
import static com.example.grocerylist.Util.Constants.DL_PRIORITY;
import static com.example.grocerylist.Util.Constants.GL_CREATE_DATE;
import static com.example.grocerylist.Util.Constants.GL_DUE_DATE;
import static com.example.grocerylist.Util.Constants.GL_NAME;
import static com.example.grocerylist.Util.Constants.GROCERY_LIST_ID;
import static com.example.grocerylist.ui.grocerylist.GroceryListViewModel.listRef;

public class GroceryListFragment extends Fragment implements NewGroceryListItemDialog.DialogSubmitListener {

    @BindView(R.id.rv_grocery_list) RecyclerView recyclerView;
    @BindView(R.id.tv_no_gl_items) TextView mNoItems;

    private GroceryListViewModel viewModel;
    private List<GroceryList> gList;
    private View root;

    private static final String TAG = GroceryListFragment.class.getSimpleName();

    public static GroceryListFragment newInstance(){
        return new GroceryListFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_grocery_list, container, false);
        ButterKnife.bind(this, root);
        viewModel = ViewModelProviders.of(this).get(GroceryListViewModel.class);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));

        recyclerView.addOnItemTouchListener(new MyTouchListener(recyclerView, new MyTouchListener.OnTouchActionListener() {
            @Override
            public void onClick(int position) {
                Log.d(TAG, "onClick " + position);
                Bundle bundle = new Bundle();
                bundle.putString(GROCERY_LIST_ID,gList.get(position).getId());
                bundle.putString(GL_NAME,gList.get(position).getListName());
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.nav_list, bundle);
            }

            @Override
            public void onSharedClicked(int position) {
                Log.d(TAG, "shared icon clicked at position: " + position);
                Bundle bundle = new Bundle();
                bundle.putString(GROCERY_LIST_ID,gList.get(position).getId());
                bundle.putString(GL_NAME,gList.get(position).getListName());
                bundle.putString(GL_CREATE_DATE,gList.get(position).getCreateDate());
                bundle.putString(GL_DUE_DATE,gList.get(position).getDueDate());

                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.nav_share, bundle);
            }
        }));

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new MyItemTouchHelper(new MyItemTouchHelper.OnItemTouchActionListener() {
            @Override
            public void onLeftSwipe(final int position) {
                final GroceryList deletedItem = gList.get(position);
                Log.d(TAG, "onLeftSwipe: " + position);
                DatabaseReference deletedRef = FirebaseDatabase.getInstance().getReference(UserUtil.getUser().getId()+"/my_list/"+deletedItem.getId());
                deletedRef.removeValue();

                Snackbar snackbar = Snackbar.make(root,"List item deleted!", Snackbar.LENGTH_LONG);
                snackbar.setAction("UNDO", v -> {
                    DatabaseReference reAddRef = FirebaseDatabase.getInstance().getReference(UserUtil.getUser().getId()+"/my_list/"+deletedItem.getId());
                    deletedItem.setId(null);
                    reAddRef.setValue(deletedItem);
                    Log.d(TAG, "re adding item");
                });
                snackbar.show();
            }

            @Override
            public void onRightSwipe(int position) {
                Log.d(TAG, "onRightSwipe " + position);
                GroceryList editItem = gList.get(position);

                // TODO: list id is null! test IT!
                Bundle args = new Bundle();
                args.putString(DLG_TITLE, "Edit Grocery List");
                args.putString(GL_NAME,editItem.getListName());
                args.putString(GROCERY_LIST_ID,editItem.getId());
                args.putString(GL_DUE_DATE,editItem.getDueDate());
                args.putInt(DL_PRIORITY,editItem.getPriority());
                args.putBoolean(DL_IS_EDITED,true);

                FragmentManager fm = getFragmentManager();
                NewGroceryListItemDialog dialog = NewGroceryListItemDialog.newInstance(args, GroceryListFragment.this);
                dialog.setTargetFragment(GroceryListFragment.this, 299);
                dialog.show(fm,"add_item_dialog2");

                recyclerView.getAdapter().notifyItemChanged(position);
            }
        }));
        itemTouchHelper.attachToRecyclerView(recyclerView);

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LiveData<List<GroceryList>> groceryList = viewModel.getGroceryList();
        groceryList.observe(this, list -> {
            Log.d(TAG, "this is changed and called");
            if(list != null && list.size() > 0){
                Log.d(TAG, "size: " + list.size());
                mNoItems.setVisibility(View.INVISIBLE);
                recyclerView.setVisibility(View.VISIBLE);
                gList = new ArrayList<>(list);
                recyclerView.setAdapter( new GroceryListRecycleViewAdapter(list));
            } else {
                mNoItems.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.INVISIBLE);
            }
        });
    }

    @OnClick(R.id.fab_grocery_list)
    public void onAddGLClicked(){
        FragmentManager fm = getFragmentManager();
        NewGroceryListItemDialog dialog = NewGroceryListItemDialog.newInstance("New Grocery List", GroceryListFragment.this);
        dialog.setTargetFragment(GroceryListFragment.this, 300);
        dialog.show(fm,"add_item_dialog1");
    }

    @Override
    public void onDialogSubmit(GroceryList groceryList, boolean isEdited) {
        String key;
        if(isEdited){
            key = groceryList.getId();
            groceryList.setId(null);
        } else {
            key = listRef.push().getKey();
        }

        Log.d(TAG, "onDialogSubmit " + groceryList.toString());

        User user = UserUtil.getUser();
        if(user != null){
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference(user.getId()+"/my_list/"+key);
            ref.setValue(groceryList);
        } else {
            Snackbar.make(getView(), "Could not add list", Snackbar.LENGTH_LONG).show();
        }
        // TODO: to show up on UI
        // update the array list before
        recyclerView.getAdapter().notifyDataSetChanged();
    }

}
