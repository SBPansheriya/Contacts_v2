package com.contacts.Fragment;

import static android.app.Activity.RESULT_OK;
import static com.contacts.Activity.HomeActivity.isGetData;
import static com.contacts.Class.Constant.usersArrayList;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.contacts.Activity.ContactDetailActivity;
import com.contacts.Activity.CreateContactActivity;
import com.contacts.Adapter.NewRecyclerAdapter;
import com.contacts.Class.Constant;
import com.contacts.Model.Users;
import com.contacts.R;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class NewRecyclerviewFragment extends Fragment {

    LinearLayout no_contcat_found_linear, Contact_found_linear;
    RecyclerView recyclerView;
    Button create_contact;
    NewRecyclerAdapter newRecyclerAdapter;
    TextView totalcontact, selectall, deselectall;
    EditText searchView;
    ProgressBar progressBar;
    ImageView edit, add_contact, cancel, share, delete;
    Users users;
    boolean isEdit = false;
    ActivityResultLauncher<Intent> launchSomeActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_fragment, container, false);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        init(view);

        checkPermission();
        getContactList();

        create_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CreateContactActivity.class);
                intent.putExtra("user", users);
                launchSomeActivity.launch(intent);
            }
        });

        add_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CreateContactActivity.class);
                intent.putExtra("user", users);
                launchSomeActivity.launch(intent);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.setVisibility(View.VISIBLE);
                add_contact.setVisibility(View.VISIBLE);
                selectall.setVisibility(View.GONE);
                deselectall.setVisibility(View.GONE);
                cancel.setVisibility(View.GONE);
                share.setVisibility(View.GONE);
                delete.setVisibility(View.GONE);
                deselectAllItems();
                newRecyclerAdapter.setEdit(false);
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.setVisibility(View.GONE);
                add_contact.setVisibility(View.GONE);
                selectall.setVisibility(View.VISIBLE);
                cancel.setVisibility(View.VISIBLE);
                share.setVisibility(View.VISIBLE);
                delete.setVisibility(View.VISIBLE);
                newRecyclerAdapter.setEdit(true);
            }
        });

        launchSomeActivity = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                Intent data = result.getData();
                if (data != null) {
                    users = (Users) result.getData().getSerializableExtra("user");
                    if (users != null) {
                        boolean isMatch = false;
                        for (int i = 0; i < usersArrayList.size(); i++) {
                            if (usersArrayList.get(i).contactId.equalsIgnoreCase(users.contactId)) {
                                isMatch = true;
                                usersArrayList.remove(i);
                                usersArrayList.add(i, users);
                                break;
                            }
                        }
                        if (!isMatch) {
                            usersArrayList.add(users);
                        }
                        getContactList();
                    }
                }
            }
            getContactList();
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Users> itemsToRemove = getSelected();

                if (itemsToRemove.isEmpty()) {
                    Toast.makeText(getContext(), "No selected contact found", Toast.LENGTH_SHORT).show();
                } else {
                    Dialog dialog = new Dialog(NewRecyclerviewFragment.this.getContext());
                    if (dialog.getWindow() != null) {
                        dialog.getWindow().setGravity(Gravity.CENTER);
                        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                        dialog.setCancelable(false);
                    }
                    dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    dialog.setContentView(R.layout.dailog_layout);
                    dialog.setCancelable(false);
                    dialog.show();

                    Button cancel1 = dialog.findViewById(R.id.canceldialog);
                    Button movetobin = dialog.findViewById(R.id.movetobin);

                    cancel1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });

                    movetobin.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            deleteSelectedItems();
                            edit.setVisibility(View.VISIBLE);
                            add_contact.setVisibility(View.VISIBLE);
                            selectall.setVisibility(View.GONE);
                            deselectall.setVisibility(View.GONE);
                            cancel.setVisibility(View.GONE);
                            share.setVisibility(View.GONE);
                            delete.setVisibility(View.GONE);
                            deselectAllItems();
                            Toast.makeText(getContext(), "Deleted contact successfully", Toast.LENGTH_SHORT).show();
                            newRecyclerAdapter.setEdit(false);
                            dialog.dismiss();
                        }
                    });
                }
            }
        });

        selectall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectAllItems();
                selectall.setVisibility(View.GONE);
                deselectall.setVisibility(View.VISIBLE);
            }
        });

        deselectall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deselectAllItems();
                selectall.setVisibility(View.VISIBLE);
                deselectall.setVisibility(View.GONE);
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareSelectedUsers();
            }
        });

//        totalcontact.setText(usersArrayList.size() + " " + "Contacts");

        searchView.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence query, int start, int before, int count) {
                filter(query.toString());
            }
        });
        return view;
    }

    private void filter(String text) {
        ArrayList<Users> filteredlist = new ArrayList<>();

        for (Users item : usersArrayList) {
            String name = item.getFirst().trim() + item.getLast().trim();
            String name1 = item.getFirst() + " " + item.getLast();
            if (item.getFirst().toLowerCase().contains(text.toLowerCase())) {
                filteredlist.add(item);
            } else if (item.getLast().toLowerCase().contains(text.toLowerCase())) {
                filteredlist.add(item);
            } else if (name.toLowerCase().contains(text.toLowerCase())) {
                filteredlist.add(item);
            } else if (name1.toLowerCase().contains(text.toLowerCase())) {
                filteredlist.add(item);
            }
        }
        if (filteredlist.size() > 0) {
            recyclerView.setVisibility(View.VISIBLE);
            newRecyclerAdapter.filterList(filteredlist);
        } else {
            recyclerView.setVisibility(View.GONE);
        }
    }


    private void deleteSelectedItems() {
        ArrayList<Users> itemsToRemove = new ArrayList<>();
        for (Users users : usersArrayList) {
            if (users.isSelected()) {
                itemsToRemove.add(users);
                deleteContact(getContext(), users.contactId);
            }
        }

        for (int i = 0; i < itemsToRemove.size(); i++) {
            for (int i1 = 0; i1 < usersArrayList.size(); i1++) {
                if (itemsToRemove.get(i).contactId.equalsIgnoreCase(usersArrayList.get(i1).contactId)) {
                    usersArrayList.remove(i1);
                    break;
                }
            }
        }
        getContactList();
    }

    public void shareSelectedUsers() {
        ArrayList<Users> selectedUsers = getSelected();

        if (selectedUsers.isEmpty()) {
            Toast.makeText(getContext(), "No selected contact found", Toast.LENGTH_SHORT).show();
        } else {
            StringBuilder shareText = new StringBuilder();
            for (Users user : selectedUsers) {
                shareText.append("Name: ").append(user.getFirst() + " " + user.getLast()).append("\n");
                shareText.append("Number: ").append(user.getPersonPhone()).append("\n");
            }

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText.toString());
            startActivity(Intent.createChooser(shareIntent, "Share Selected Users"));
        }
    }

    public ArrayList<Users> getSelected() {
        ArrayList<Users> selected = new ArrayList<>();
        for (Users u : usersArrayList) {
            if (u.isSelected()) {
                selected.add(u);
            }
        }
        return selected;
    }

    private void selectAllItems() {
        for (Users item : usersArrayList) {
            item.setSelected(true);
        }
        newRecyclerAdapter.notifyDataSetChanged();
    }

    private void deselectAllItems() {
        for (Users item : usersArrayList) {
            item.setSelected(false);
        }
        newRecyclerAdapter.notifyDataSetChanged();
    }

    public static void deleteContact(Context context, String contactId) {
        ContentResolver contentResolver = context.getContentResolver();
        Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long.parseLong(contactId));

        contentResolver.delete(ContactsContract.RawContacts.CONTENT_URI, ContactsContract.RawContacts.CONTACT_ID + " = ?", new String[]{String.valueOf(contactId)});

        contentResolver.delete(contactUri, null, null);
    }

    public void intentPass(Users users) {
        Intent intent = new Intent(getActivity(), ContactDetailActivity.class);
        intent.putExtra("user", users);
        launchSomeActivity.launch(intent);
    }

    private void getContactList() {
        if (usersArrayList.size() > 0) {
            no_contcat_found_linear.setVisibility(View.GONE);
            Contact_found_linear.setVisibility(View.VISIBLE);
            searchView.setVisibility(View.VISIBLE);
            edit.setVisibility(View.VISIBLE);
            add_contact.setVisibility(View.VISIBLE);

            Comparator<Users> nameComparator = Comparator.comparing(Users::getFirst);
            usersArrayList.sort(nameComparator);

            LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            newRecyclerAdapter = new NewRecyclerAdapter(NewRecyclerviewFragment.this, usersArrayList, isEdit);
            recyclerView.setLayoutManager(manager);
            recyclerView.setAdapter(newRecyclerAdapter);

            totalcontact.setText(usersArrayList.size() + " " + "Contacts");
        }
        else {
            no_contcat_found_linear.setVisibility(View.VISIBLE);
            Contact_found_linear.setVisibility(View.GONE);
            searchView.setVisibility(View.GONE);
            edit.setVisibility(View.GONE);
            add_contact.setVisibility(View.GONE);
        }

    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_CONTACTS}, 100);
        } else {
            getContactList();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getContactList();
            } else {
                checkPermission();
            }
        }
    }

    private void init(View view) {
        recyclerView = view.findViewById(R.id.Newrecyclerview);
        totalcontact = view.findViewById(R.id.totalcontact);
        searchView = view.findViewById(R.id.search_contact);
        edit = view.findViewById(R.id.edit);
        add_contact = view.findViewById(R.id.add_contact);
        cancel = view.findViewById(R.id.cancel);
        share = view.findViewById(R.id.share);
        delete = view.findViewById(R.id.trash);
        selectall = view.findViewById(R.id.selectall);
        deselectall = view.findViewById(R.id.deselectall);
        no_contcat_found_linear = view.findViewById(R.id.no_contcat_found_linear);
        Contact_found_linear = view.findViewById(R.id.Contact_found_linear);
        progressBar = view.findViewById(R.id.progress_circular);
        create_contact = view.findViewById(R.id.create_contact);
    }
}