package com.example.grocerylist.ui.grocerylist;

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
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grocerylist.adapters.GroceryListRecycleViewAdapter;
import com.example.grocerylist.adapters.MyItemTouchHelper;
import com.example.grocerylist.adapters.MyTouchListener;
import com.example.grocerylist.R;
import com.example.grocerylist.util.UserUtil;
import com.example.grocerylist.ui.dialogs.NewGroceryListItemDialog;
import com.example.grocerylist.entities.GroceryList;
import com.example.grocerylist.entities.User;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

import static com.example.grocerylist.util.Constants.DLG_TITLE;
import static com.example.grocerylist.util.Constants.DL_PRIORITY;
import static com.example.grocerylist.util.Constants.GL_CREATE_DATE;
import static com.example.grocerylist.util.Constants.GL_DUE_DATE;
import static com.example.grocerylist.util.Constants.GL_NAME;
import static com.example.grocerylist.util.Constants.GL_POSITION;
import static com.example.grocerylist.util.Constants.GROCERY_LIST_ID;
import static com.example.grocerylist.ui.grocerylist.GroceryListViewModel.listRef;

/**
 * Grocery List fragment implements the necessary interfaces
 */
public class GroceryListFragment
        extends Fragment
        implements NewGroceryListItemDialog.DialogSubmitListener,
                MyTouchListener.OnTouchActionListener,
                MyItemTouchHelper.OnItemTouchActionListener {

    // Binding views via ButterKnife
    @BindView(R.id.rv_grocery_list) RecyclerView recyclerView;
    @BindView(R.id.tv_no_gl_items) TextView mNoItems;
    @BindView(R.id.fab_grocery_list)
    FloatingActionButton mFab;
    @BindView(R.id.adView)
    AdView mAdview;

    private static GroceryListViewModel viewModel;
    private List<GroceryList> gList = new ArrayList<>();
    private View root;

    private static final String TAG = GroceryListFragment.class.getSimpleName();

    /**
     * For creating new instance of the fragment class
     * @return new instance of the class
     */
    public static GroceryListFragment newInstance(){
        return new GroceryListFragment();
    }

    /**
     * OnCreate method required by Fragment class
     * @param inflater layout inflater to inflate resource layout file
     * @param container
     * @param savedInstanceState
     * @return the root view
     */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_grocery_list, container, false);
        viewModel = ViewModelProviders.of(this).get(GroceryListViewModel.class);
        ButterKnife.bind(this, root);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));

        recyclerView.addOnItemTouchListener(new MyTouchListener(recyclerView, GroceryListFragment.this));

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new MyItemTouchHelper(GroceryListFragment.this));
        itemTouchHelper.attachToRecyclerView(recyclerView);

        MobileAds.initialize(getContext(),"ca-app-pub-3940256099942544/3347511713");
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
        mAdview.setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                Timber.d( "Failed to load ad %s", i);
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
     * OnActivityCreated method of the fragment
     * Sets up the viewModel and adds the observable with live data
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LiveData<List<GroceryList>> groceryList = viewModel.getGroceryList();
        groceryList.observe(this, list -> {
            Timber.d("onActivityCreated");
            if(list != null && list.size() > 0){
                Timber.d( "size: %s", list.size());
                mNoItems.setVisibility(View.INVISIBLE);
                recyclerView.setVisibility(View.VISIBLE);
            } else {
                mNoItems.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.INVISIBLE);
            }
            gList = new ArrayList<>(list);
            recyclerView.setAdapter( new GroceryListRecycleViewAdapter(list));
            boolean isTablet = getResources().getBoolean(R.bool.tablet);
            if(isTablet && list.size()>0){
                Bundle bundle = new Bundle();
                bundle.putString(GROCERY_LIST_ID,gList.get(0).getId());
                bundle.putString(GL_NAME,gList.get(0).getListName());
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.nav_list, bundle);
            }
        });
    }

    /**
     * Method to open the dialog to add new item
     * Binding with ButterKnife OnClick
     */
    @OnClick(R.id.fab_grocery_list)
    public void onAddGLClicked(){
        Bundle args = new Bundle();
        args.putString(DLG_TITLE, "New Grocery List");
        openGLDialog(args);
    }

    /**
     * Opens a dialog for adding or editing GL list item
     * @param args
     *      title: dialog title (required)
     *      If item is edited:
     *      GL_NAME: GL name
     *      GROCERY_LIST_ID: GL id (firebase unique id)
     *      GL_DUE_DATE: GL due date
     *      DL_PRIORITY: GL priority (int of the @arrays/unit_measure_imperial|metric position)
     *      GL_POSITION: position in the list
     *
     */
    private void openGLDialog(Bundle args) {
        FragmentManager fm = getFragmentManager();
        NewGroceryListItemDialog dialog = NewGroceryListItemDialog.newInstance(args, GroceryListFragment.this);
        dialog.setTargetFragment(GroceryListFragment.this, 299);
        dialog.show(fm,"add_item_dialog");
    }

    /**
     * onResume to add the animation to the fab
     */
    @Override
    public void onResume() {
        super.onResume();
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.fab_anim);
        animation.setStartOffset(500);
        mFab.startAnimation(animation);
    }

    /**
     * Overridden method of the dialog to allow user to add new list item or edit
     * @param groceryList new or edited item
     * @param position position of the item was edited or -1 if the item is new
     */
    @Override
    public void onDialogSubmit(GroceryList groceryList, int position) {
        gList.add(groceryList);
        String key;

        // -1 == new item
        if(position != -1){
            key = groceryList.getId();
            gList.set(position,groceryList);
            recyclerView.getAdapter().notifyItemChanged(position);
        } else { // new item
            key = listRef.push().getKey();
            groceryList.setId(key);
            gList.add(groceryList);
            //recyclerView.getAdapter().notifyItemInserted(gList.size()-1);
        }

        Timber.d( "onDialogSubmit %s", groceryList.toString());

        User user = UserUtil.getUser();
        if(user != null){
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference(user.getId()+"/my_list/"+key);
            groceryList.setId(null);
            ref.setValue(groceryList);
        } else {
            Snackbar.make(getView(), "Could not add list", Snackbar.LENGTH_LONG).show();
        }
    }

    /**
     * Overridden method of @MyItemTouchHelper to delete on left swipe
     * This will allow user to undo the delete
     * @param position of the item to be deleted
     */
    @Override
    public void onLeftSwipe(int position) {
        // Deleting
        final GroceryList deletedItem = gList.get(position);
        Timber.d( "onLeftSwipe: %s", position);
        DatabaseReference deletedRef = FirebaseDatabase.getInstance().getReference(UserUtil.getUser().getId()+"/my_list/"+deletedItem.getId());
        deletedRef.removeValue();

        Snackbar snackbar = Snackbar.make(root,"List item deleted!", Snackbar.LENGTH_LONG);
        snackbar.setAction("UNDO", v -> {
            DatabaseReference reAddRef = FirebaseDatabase.getInstance().getReference(UserUtil.getUser().getId()+"/my_list/"+deletedItem.getId());
            deletedItem.setId(null);
            reAddRef.setValue(deletedItem);
            Timber.d( "re adding item");
        });
        snackbar.show();
    }

    /**
     * Overridden method of @MyItemTouchHelper to edit on right swipe
     * @param position of the item to be edited
     */
    @Override
    public void onRightSwipe(int position) {
        // Editing
        Timber.d("onRightSwipe %s", position);
        GroceryList editItem = gList.get(position);

        Bundle args = new Bundle();
        args.putString(DLG_TITLE, "Edit Grocery List");
        args.putString(GL_NAME,editItem.getListName());
        args.putString(GROCERY_LIST_ID,editItem.getId());
        args.putString(GL_DUE_DATE,editItem.getDueDate());
        args.putInt(DL_PRIORITY,editItem.getPriority());
        args.putInt(GL_POSITION,position);

        openGLDialog(args);
        recyclerView.getAdapter().notifyItemChanged(position);
    }

    /**
     * Overridden method to open the list and display the list items
     * @param position of the item to be displayed
     */
    @Override
    public void onClick(int position) {
        Timber.d( "onClick %s", position);
        Bundle bundle = new Bundle();
        bundle.putString(GROCERY_LIST_ID,gList.get(position).getId());
        bundle.putString(GL_NAME,gList.get(position).getListName());
        Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.nav_list, bundle);
    }

    /**
     * Overridden method to share the list either via email or among GL users
     * @param position of the item in the list to be shared
     */
    @Override
    public void onSharedClicked(int position) {
        Timber.d( "shared icon clicked at position: %s", position);
        Bundle bundle = new Bundle();
        bundle.putString(GROCERY_LIST_ID,gList.get(position).getId());
        bundle.putString(GL_NAME,gList.get(position).getListName());
        bundle.putString(GL_CREATE_DATE,gList.get(position).getCreateDate());
        bundle.putString(GL_DUE_DATE,gList.get(position).getDueDate());

        Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.nav_share, bundle);
    }
}
