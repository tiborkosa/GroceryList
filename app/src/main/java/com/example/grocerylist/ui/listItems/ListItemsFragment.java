package com.example.grocerylist.ui.listItems;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grocerylist.MainActivity;
import com.example.grocerylist.adapters.ListItemsRecycleViewAdapter;
import com.example.grocerylist.adapters.MyItemTouchHelper;
import com.example.grocerylist.R;
import com.example.grocerylist.util.MyViewModelFactory;
import com.example.grocerylist.ui.dialogs.NewListItemDialog;
import com.example.grocerylist.entities.ListItem;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

import static com.example.grocerylist.util.Constants.DLG_TITLE;
import static com.example.grocerylist.util.Constants.GL_NAME;
import static com.example.grocerylist.util.Constants.GROCERY_LIST_ID;
import static com.example.grocerylist.util.Constants.ITEM_ID;
import static com.example.grocerylist.util.Constants.ITEM_NAME;
import static com.example.grocerylist.util.Constants.ITEM_POSITION;
import static com.example.grocerylist.util.Constants.ITEM_QUANTITY;
import static com.example.grocerylist.util.Constants.ITEM_UNIT_OF_MEASURE;
import static com.example.grocerylist.ui.listItems.ListItemsViewModel.List_Items_Ref;

/**
 * List item fragment that implements the needed interfaces
 */
public class ListItemsFragment
        extends Fragment
        implements NewListItemDialog.DialogSubmitListener,
                   MyItemTouchHelper.OnItemTouchActionListener,
                   ListItemsRecycleViewAdapter.OnItemClicked{

    private ListItemsViewModel viewModel;
    private static final String TAG = ListItemsFragment.class.getSimpleName();

    /**
     * for getting a new instance of the fragment
     * @param args to set up needed fields
     * @return new fragment
     */
    public static ListItemsFragment getInstance(Bundle args) {
        ListItemsFragment frag = new ListItemsFragment();
        frag.setArguments(args);
        return frag;
    }

    @BindView(R.id.rv_list_items)
    RecyclerView recyclerView;
    @BindView(R.id.tv_no_items)
    TextView mNoItems;
    @BindView(R.id.fab_add_list_item)
    FloatingActionButton mFab;
    @BindView(R.id.adViewList)
    AdView mAdview;

    /**
     * onCreateView to inflate the view
     * @param inflater layout inflater
     * @param container where we want to inflate the fragment
     * @param savedInstanceState saved instance state
     * @return new inflated view
     */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_list_items, container, false);

        if(getArguments() != null && getArguments().containsKey(GL_NAME)) {
            ((MainActivity) getActivity()).getSupportActionBar().setTitle(getArguments().getString(GL_NAME, "Grocery List"));
        }

        viewModel =
                ViewModelProviders
                        .of(this,
                                new MyViewModelFactory(getArguments()))
                        .get(ListItemsViewModel.class);

        ButterKnife.bind(this, root);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new MyItemTouchHelper(ListItemsFragment.this));
        itemTouchHelper.attachToRecyclerView(recyclerView);

        MobileAds.initialize(getContext(),"ca-app-pub-3940256099942544/3347511713");
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
        mAdview.setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                Timber.d("Failed to load ad %s", i);
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                Timber.d( "onAdLoaded");
            }
        });
        mAdview.loadAd(adRequest);

        return root;
    }

    /**
     * when the activity created we attach the live data observer and populate the fields
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LiveData<List<ListItem>> listLiveData = viewModel.getItemsList();
        listLiveData.observe(this, (items) -> {
                    if (items != null && items.size() > 0) {
                        mNoItems.setVisibility(View.INVISIBLE);
                        recyclerView.setVisibility(View.VISIBLE);
                    } else {
                        mNoItems.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.INVISIBLE);
                    }
                    recyclerView.setAdapter(new ListItemsRecycleViewAdapter(items, ListItemsFragment.this));
                }
        );
    }

    /**
     * Open dialog to create a new list item
     * Using butterknife for binding the onclick
     */
    @OnClick(R.id.fab_add_list_item)
    public void onNewItemClicked() {
        Timber.d( " fab is clicked.");
        Bundle args = new Bundle();
        args.putString(DLG_TITLE, "New Item");
        openDialog(args);
    }

    /**
     * helper method to open the dialog
     * @param args
     */
    private void openDialog(Bundle args){
        FragmentManager fm = getFragmentManager();
        NewListItemDialog dialog = NewListItemDialog
                .newInstance(args, ListItemsFragment.this);
        dialog.setTargetFragment(ListItemsFragment.this, 301);
        dialog.show(fm, "listItem_dlg");
    }

    /**
     * adding animation
     */
    @Override
    public void onResume() {
        super.onResume();
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.fab_anim);
        animation.setStartOffset(500);
        mFab.startAnimation(animation);
    }

    /**
     * overridden interface of the dialog's callback
     * @param listItem item that was created or updated
     * @param position position of the updated item or -1 if new
     */
    @Override
    public void onDialogSubmit(ListItem listItem, int position) {
        String listId = listItem.getId();

        ListItem newListItem = copyListItem(listItem);

        if (-1 != position) {   // update
            viewModel.updateItem(newListItem, position);
            // update db
            DatabaseReference ref = FirebaseDatabase
                    .getInstance()
                    .getReference("/g_list_items/" + getArguments().getString(GROCERY_LIST_ID) + "/" + listId);
            listItem.setId(null);
            ref.setValue(listItem);
        } else {    // new item
            if (getArguments() != null && getArguments().containsKey(GROCERY_LIST_ID)) {
                // Save item in the db
                DatabaseReference dbRef = List_Items_Ref.push();
                String id = dbRef.getKey();
                dbRef.setValue(listItem);
                // update the view without getting it from the db
                newListItem.setId(id);
                ListItemsViewModel.addNewItem(newListItem);
            } else {
                Snackbar.make(getView(), "Could not add the item!", Snackbar.LENGTH_LONG).show();
            }
        }
    }

    /**
     * overridden interface of @MyTouchListener
     * when left swiped item will be deleted
     * @param position of the item swiped
     */
    @Override
    public void onLeftSwipe(int position) {
        Timber.d( "Left Swipe happened to delete");
        String parentId = getArguments().getString(GROCERY_LIST_ID, null);
        if (parentId != null) {
            DatabaseReference ref = FirebaseDatabase
                    .getInstance()
                    .getReference("/g_list_items/" + parentId + "/" + viewModel
                            .getItemsList()
                            .getValue()
                            .get(position)
                            .getId());
            ref.removeValue();
            ListItemsViewModel.deleteItem(position);
        }
        // TODO: add undo maybe

    }

    /**
     * overridden interface of @MyTouchListener
     * when right swiped item will be edited
     * @param position of the item swiped
     */
    @Override
    public void onRightSwipe(int position) {
        Timber.d("Right Swipe happened to edit item");
        ListItem toUpdateItem = viewModel.getItemsList().getValue().get(position);
        recyclerView.getAdapter().notifyItemChanged(position);
        if(toUpdateItem.isPurchased()){
            Snackbar.make(getView(), "Item is already purchased!", Snackbar.LENGTH_LONG).show();
            return;
        }

        Bundle args = new Bundle();
        args.putString(ITEM_ID, toUpdateItem.getId());
        args.putString(ITEM_NAME, toUpdateItem.getName());
        args.putInt(ITEM_UNIT_OF_MEASURE, toUpdateItem.getMeasure());
        args.putDouble(ITEM_QUANTITY, toUpdateItem.getQuantity());
        args.putInt(ITEM_POSITION, position);
        args.putString(DLG_TITLE, "Edit Item");

        openDialog(args);
    }

    /**
     * overridden interface of the @ListItemRecyclerViewAdapter
     * @param listItem that was purchased or un purchased
     * @param position position of the item
     */
    @Override
    public void onCheckBoxClicked(ListItem listItem, int position) {
        Timber.d( "listItem: %s", listItem.toString() );

        String gl_id = getArguments().getString(GROCERY_LIST_ID, null);
        if (gl_id != null) {
            FirebaseDatabase
                    .getInstance()
                    .getReference("/g_list_items/" + gl_id + "/" + listItem.getId() + "/purchased").setValue(listItem.isPurchased());
            // update UI
            viewModel.updateItem(listItem, position);
        }
    }

    /**
     * helper function to avoid reference issues when setting the id to null
     * @param oldItem to be transformed
     * @return new ListItem created
     */
    private ListItem copyListItem(ListItem oldItem){
        return new ListItem(
                oldItem.getId(),
                oldItem.getName(),
                oldItem.getQuantity(),
                oldItem.getMeasure(),
                oldItem.isPurchased());
    }
}
