package com.contacts.Fragment;

import static androidx.core.content.PermissionChecker.checkSelfPermission;
import static com.contacts.Activity.HomeActivity.isGetData;
import static com.contacts.Class.Constant.recentArrayList;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.IntentFilter;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.Settings;
import android.telephony.TelephonyManager;
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

import com.contacts.Adapter.RecentListAdapter;
import com.contacts.Activity.KeypadScreen;
import com.contacts.Model.Recent;
import com.contacts.Activity.PhoneStateBroadcastReceiver;
import com.contacts.R;

public class RecentsFragment extends Fragment implements PhoneStateBroadcastReceiver.CallListener {

    RecentListAdapter recentListAdapter;
    RecyclerView recyclerView;
    ImageView open_keypad;
    LinearLayout no_recents_linear;
    ProgressBar progressBar;
    Recent recent;
    ActivityResultLauncher<Intent> launchSomeActivity;
    private PhoneStateBroadcastReceiver phoneStateReceiver;
    String selectedPhoneNUmber;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recents, container, false);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        init(view);

        checkPermission();
        getRecentContacts();

        IntentFilter intentFilter = new IntentFilter(TelephonyManager.ACTION_PHONE_STATE_CHANGED);
        phoneStateReceiver = new PhoneStateBroadcastReceiver();
        phoneStateReceiver.callListener = this::callEnd;
        getContext().registerReceiver(phoneStateReceiver, intentFilter);

        if (recentArrayList.isEmpty()) {
            no_recents_linear.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
        }
        launchSomeActivity = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                if (data != null) {
                    recent = (Recent) result.getData().getSerializableExtra("recents");
                    if (recent != null) {
                        recentArrayList.add(recent);
                        getRecentContacts();
                    }
                }
            }
        });

        open_keypad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), KeypadScreen.class);
                intent.putExtra("check", "recents");
                startActivity(intent);
            }
        });
        return view;
    }

    public void getRecentContacts() {
        if (recentListAdapter != null) {
            recentListAdapter.setRecentArrayList(recentArrayList);
        } else {
            recentListAdapter = new RecentListAdapter(RecentsFragment.this, recentArrayList);
            LinearLayoutManager manager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(manager);
            recyclerView.setAdapter(recentListAdapter);
        }
    }

    private void checkPermission() {
        String[] permissions = new String[]{Manifest.permission.READ_CALL_LOG};

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), permissions, 101);
        } else {
            getRecentContacts();
        }
    }

    public boolean checkPermission1() {
        return ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_DENIED;
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (!TextUtils.isEmpty(selectedPhoneNUmber)) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + selectedPhoneNUmber));
                startActivity(intent);
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
                    Toast.makeText(RecentsFragment.this.getContext(), "Setting", Toast.LENGTH_SHORT).show();
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

    private void init(View view) {
        recyclerView = view.findViewById(R.id.recents_recyclerview);
        no_recents_linear = view.findViewById(R.id.no_recents);
        open_keypad = view.findViewById(R.id.open_keypad);
        progressBar = view.findViewById(R.id.progress_circular);
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

    @Override
    public void callEnd() {
        getRecentContacts();
    }
}