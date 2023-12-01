package com.contacts.Fragment;

import static androidx.core.content.PermissionChecker.checkSelfPermission;
import static com.contacts.Activity.HomeActivity.isGetData;
import static com.contacts.Class.Constant.favoriteList;
import static com.contacts.Class.Constant.usersArrayList;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.Settings;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.contacts.Activity.AddFavouritesActivity;
import com.contacts.Activity.ContactDetailActivity;
import com.contacts.Adapter.FavListAdapter;
import com.contacts.Activity.KeypadScreen;
import com.contacts.Model.Users;
import com.contacts.R;

public class FavoritesFragment extends Fragment {

    FavListAdapter favListAdapter;
    RecyclerView recyclerView;
    TextView add_fav_contact;
    ImageView open_keypad;
    LinearLayout no_fav_found_linear;
    TextView done;
    Button addFav;
    ImageView edit;
    ProgressBar progressBar;
    Users users;
    String selectedPhoneNUmber;
    ActivityResultLauncher<Intent> launchSomeActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        init(view);

        checkPermission();
        readFavoriteContacts();

        addFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddFavouritesActivity.class);
                launchSomeActivity.launch(intent);
            }
        });

        open_keypad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), KeypadScreen.class);
                intent.putExtra("check", "fav");
                startActivity(intent);
            }
        });

        add_fav_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddFavouritesActivity.class);
                launchSomeActivity.launch(intent);
            }
        });

        launchSomeActivity = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                if (data != null) {
                    users = (Users) result.getData().getSerializableExtra("user");
                    if (users != null) {
                        for (int i = 0; i < favoriteList.size(); i++) {
                            if (favoriteList.get(i).contactId.equalsIgnoreCase(users.contactId)) {
                                favoriteList.remove(i);
                                favoriteList.add(i, users);
                                break;
                            }
                        }
                        readFavoriteContacts();
                    }
                }
            }
            readFavoriteContacts();
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
//        });
        return view;
    }

    public boolean checkPermission1() {
        return ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_DENIED;
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_CONTACTS}, 101);
        } else {
            readFavoriteContacts();
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (!TextUtils.isEmpty(selectedPhoneNUmber)) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + selectedPhoneNUmber));
                startActivity(intent);
            }
        } else if (requestCode == 101) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                readFavoriteContacts();
            }
        } else {
            showPermissionDialog();
        }
    }

    private void showPermissionDialog() {
        Dialog dialog = new Dialog(getContext());
        if (dialog.getWindow() != null) {
            dialog.getWindow().setGravity(Gravity.CENTER);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.setCancelable(false);
        }
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.setContentView(R.layout.dialog_permission);
        dialog.setCancelable(false);
        dialog.show();

        Button gotosettings = dialog.findViewById(R.id.gotosettings);
        ImageView dismiss_dialog = dialog.findViewById(R.id.dismiss_dialog);
        dismiss_dialog.setVisibility(View.VISIBLE);
        TextView textView = dialog.findViewById(R.id.txt1);

        textView.setText("This app needs Call phone permissions to use this feature. You can grant them in app settings.");

        dismiss_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        gotosettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE)) {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                    intent.setData(uri);
                    startActivityForResult(intent, 100);
                    Toast.makeText(FavoritesFragment.this.getContext(), "Setting", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == 100) {
                checkPermission1();
            }
        } catch (Exception ex) {
            Toast.makeText(getContext(), ex.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void call(String phoneNumber) {
        selectedPhoneNUmber = phoneNumber;
        String[] permissions = new String[]{Manifest.permission.CALL_PHONE};
        if (checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) == PermissionChecker.PERMISSION_DENIED) {
            requestPermissions(permissions, 100);
        } else {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
            startActivity(intent);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void readFavoriteContacts() {
        if (favoriteList.size() > 0) {
            LinearLayoutManager manager = new LinearLayoutManager(getContext());
            favListAdapter = new FavListAdapter(FavoritesFragment.this, favoriteList);
            recyclerView.setLayoutManager(manager);
            recyclerView.setAdapter(favListAdapter);
            favListAdapter.notifyDataSetChanged();
            no_fav_found_linear.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            edit.setVisibility(View.VISIBLE);
            add_fav_contact.setVisibility(View.VISIBLE);
        } else {
            no_fav_found_linear.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            edit.setVisibility(View.GONE);
            add_fav_contact.setVisibility(View.GONE);
        }
    }

    public void intentPassFav(Users users) {
        Intent intent = new Intent(getActivity(), ContactDetailActivity.class);
        intent.putExtra("user", users);
        launchSomeActivity.launch(intent);
    }

    private void init(View view) {
        addFav = view.findViewById(R.id.create_fav);
        edit = view.findViewById(R.id.fav_edit);
//        done = view.findViewById(R.id.add_fav_contact);
        recyclerView = view.findViewById(R.id.recyclerView);
        no_fav_found_linear = view.findViewById(R.id.no_fav_found_linear);
        add_fav_contact = view.findViewById(R.id.add_fav_contact);
        open_keypad = view.findViewById(R.id.open_keypad);
        progressBar = view.findViewById(R.id.progress_circular);
    }
}