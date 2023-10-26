package com.contacts.Fragment;

import static com.contacts.Class.Constant.favoriteList;
import static com.contacts.Class.Constant.usersArrayList;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.contacts.Adapter.FavListAdapter;
import com.contacts.Activity.HomeActivity;
import com.contacts.Adapter.ItemTouchHelperCallback;
import com.contacts.Model.Users;
import com.contacts.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class FavoritesFragment extends Fragment {

    FavListAdapter favListAdapter;
    RecyclerView recyclerView;
    FloatingActionButton add_fav_contact;
    LinearLayout no_fav_found_linear;
    TextView done;
    ImageView back;
    Button addfav;
    ImageView edit;
    Users users;
    ActivityResultLauncher<Intent> launchSomeActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);

        init(view);

        checkPermission();

        if (favoriteList.isEmpty()) {
            no_fav_found_linear.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
            edit.setVisibility(View.INVISIBLE);
            add_fav_contact.setVisibility(View.INVISIBLE);
        }

        launchSomeActivity = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                // do your operation from here....
                if (data != null) {
                    users = (Users) result.getData().getSerializableExtra("user");
                    if (users != null) {
                        favoriteList.add(users);
//                        boolean isMatch = false;
//                        for (int i = 0; i < usersArrayList.size(); i++) {
//                            if (usersArrayList.get(i).contactId.equalsIgnoreCase(users.contactId)) {
//                                isMatch = true;
//                                usersArrayList.remove(i);
//                                usersArrayList.add(i, users);
//                                break;
//                            }
//                        }
//                        if (!isMatch) {
//                            usersArrayList.add(users);
//                        }
                        readFavoriteContacts();
                    }
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), HomeActivity.class);
                startActivity(intent);
            }
        });

        addfav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment mFragment = new ContactsFragment();
                Bundle bundle = new Bundle();
                bundle.putString("btn", "no_fav_found");
                mFragment.setArguments(bundle);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.framelayout, mFragment)
                        .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .commit();
            }
        });

        add_fav_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment mFragment = new ContactsFragment();
                Bundle bundle = new Bundle();
                bundle.putString("btn", "fav");
//                launchSomeActivity.launch(bundle);
                mFragment.setArguments(bundle);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.framelayout, mFragment)
                        .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .commit();
            }
        });
//        edit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                edit.setVisibility(View.GONE);
//                done.setVisibility(View.VISIBLE);
//                scrollcontact.setVisibility(View.VISIBLE);
//                info_icon.setVisibility(View.GONE);
//            }
//        });

//        done.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onDestroyView();
//            }
//        });\
        return view;
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_CONTACTS}, 100);
        } else {
            readFavoriteContacts();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                readFavoriteContacts();
            } else {
                Toast.makeText(getContext(), "Permission denied. Cannot read contacts.", Toast.LENGTH_SHORT).show();
                checkPermission();
            }
        }
    }

    private void readFavoriteContacts() {
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        favListAdapter = new FavListAdapter(FavoritesFragment.this, favoriteList);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(favListAdapter);
        favListAdapter.notifyDataSetChanged();
    }

    private void init(View view) {
        back = view.findViewById(R.id.back);
        addfav = view.findViewById(R.id.create_fav);
        edit = view.findViewById(R.id.fav_edit);
        done = view.findViewById(R.id.fav_done);
        recyclerView = view.findViewById(R.id.recyclerView);
        no_fav_found_linear = view.findViewById(R.id.no_fav_found_linear);
        add_fav_contact = view.findViewById(R.id.add_fav_contact);
    }
}