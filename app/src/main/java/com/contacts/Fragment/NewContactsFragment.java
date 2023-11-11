package com.contacts.Fragment;

import static android.app.Activity.RESULT_OK;
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
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.contacts.Activity.ContactDetailActivity;
import com.contacts.Activity.CreateContactActivity;
import com.contacts.Adapter.NewAdapter;
import com.contacts.Class.Constant;
import com.contacts.Model.Users;
import com.contacts.R;
import com.reddit.indicatorfastscroll.FastScrollerThumbView;
import com.reddit.indicatorfastscroll.FastScrollerView;

import java.util.ArrayList;
import java.util.Comparator;

public class NewContactsFragment extends Fragment {

    RecyclerView recyclerView;
    RelativeLayout tbmenu;
    LinearLayout no_contcat_found_linear, Contact_found_linear;
    ImageView edit, cancel, share, delete, back;
    Button create_btn;
    SearchView searchView;
    TextView selectall, totalcontact, deselectall;
    ImageView add_contact;
    ViewGroup viewGroup;
    Users users;
    ActivityResultLauncher<Intent> launchSomeActivity;
    RelativeLayout progressLayout;
    FastScrollerView fastScrollerView;
    FastScrollerThumbView fastScrollerThumbView;
    String button = "";
    NewAdapter newAdapter;
    boolean isEdit = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);

        init(view);

        button = getArguments().getString("btn");

        if (button.equals("fav")) {
            tbmenu.setVisibility(View.VISIBLE);
            edit.setVisibility(View.GONE);
            add_contact.setVisibility(View.GONE);
        }
        if (button.equals("no_fav_found")) {
            tbmenu.setVisibility(View.VISIBLE);
            edit.setVisibility(View.GONE);
            add_contact.setVisibility(View.GONE);
        }
        if (button.equals("contact")) {
            tbmenu.setVisibility(View.GONE);
        }

        checkPermission();

//        launchSomeActivity = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
//            if (result.getResultCode() == RESULT_OK) {
//                Intent data = result.getData();
//                if (data != null) {
//                    users = (Users) result.getData().getSerializableExtra("user");
//                    if (users != null) {
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
//                        getContactList();
//                    }
//                }
//            }
//        });
//
//        add_contact.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getActivity(), CreateContactActivity.class);
//                intent.putExtra("user", users);
//                launchSomeActivity.launch(intent);
//            }
//        });
//
//        edit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                edit.setVisibility(View.GONE);
//                add_contact.setVisibility(View.GONE);
//                selectall.setVisibility(View.VISIBLE);
//                cancel.setVisibility(View.VISIBLE);
//                share.setVisibility(View.VISIBLE);
//                delete.setVisibility(View.VISIBLE);
//                newAdapter.setEdit(true);
//            }
//        });
//
//        cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                edit.setVisibility(View.VISIBLE);
//                add_contact.setVisibility(View.VISIBLE);
//                selectall.setVisibility(View.GONE);
//                deselectall.setVisibility(View.GONE);
//                cancel.setVisibility(View.GONE);
//                share.setVisibility(View.GONE);
//                delete.setVisibility(View.GONE);
//                deselectAllItems();
//                newAdapter.setEdit(false);
//            }
//        });
//
//        back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Fragment mFragment = new FavoritesFragment();
//                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, mFragment).setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
//            }
//        });
//
//        create_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getActivity(), CreateContactActivity.class);
//                intent.putExtra("user", users);
//                launchSomeActivity.launch(intent);
//            }
//        });
//
//        if (usersArrayList.isEmpty()) {
//            no_contcat_found_linear.setVisibility(View.VISIBLE);
//            Contact_found_linear.setVisibility(View.INVISIBLE);
//        }
//
//        totalcontact.setText(usersArrayList.size() + " " + "Contacts");
//
//        delete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ArrayList<Users> itemsToRemove = getSelected();
//
//                if (itemsToRemove.isEmpty()) {
//                    Toast.makeText(getContext(), "No selected contact found", Toast.LENGTH_SHORT).show();
//                } else {
//                    Dialog dialog = new Dialog(NewContactsFragment.this.getContext());
//                    if (dialog.getWindow() != null) {
//                        dialog.getWindow().setGravity(Gravity.CENTER);
//                        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//                        dialog.setCancelable(false);
//                    }
//                    dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//                    dialog.setContentView(R.layout.dailog_layout);
//                    dialog.setCancelable(false);
//                    dialog.show();
//
//                    Button cancel1 = dialog.findViewById(R.id.canceldialog);
//                    Button movetobin = dialog.findViewById(R.id.movetobin);
//
//                    cancel1.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            dialog.dismiss();
//                        }
//                    });
//
//                    movetobin.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            deleteSelectedItems();
//                            edit.setVisibility(View.VISIBLE);
//                            add_contact.setVisibility(View.VISIBLE);
//                            selectall.setVisibility(View.GONE);
//                            deselectall.setVisibility(View.GONE);
//                            cancel.setVisibility(View.GONE);
//                            share.setVisibility(View.GONE);
//                            delete.setVisibility(View.GONE);
//                            deselectAllItems();
//                            Toast.makeText(getContext(), "Deleted contact successfully", Toast.LENGTH_SHORT).show();
//                            newAdapter.setEdit(false);
//                            dialog.dismiss();
//                        }
//                    });
//                }
//            }
//        });
//
//        selectall.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                selectAllItems();
//                selectall.setVisibility(View.GONE);
//                deselectall.setVisibility(View.VISIBLE);
//            }
//        });
//
//        deselectall.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                deselectAllItems();
//                selectall.setVisibility(View.VISIBLE);
//                deselectall.setVisibility(View.GONE);
//            }
//        });
//
//        share.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                shareSelectedUsers();
//            }
//        });
//
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                filterContacts(newText);
//                return true;
//            }
//        });
        return view;
    }

    public void filterContacts(String query) {

        query = query.toString().toLowerCase();

        ArrayList<Users> filteredList = new ArrayList<>();

        for (int i = 0; i < Constant.usersArrayList.size(); i++) {
            final String name = Constant.usersArrayList.get(i).getFirst().toLowerCase();
            if (name.contains(query)) {
                filteredList.add(Constant.usersArrayList.get(i));
            }
        }
        if (filteredList.size() > 0) {
            recyclerView.setVisibility(View.VISIBLE);
            newAdapter.setFilteredList(filteredList);
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
        newAdapter.notifyDataSetChanged();
    }

    private void deselectAllItems() {
        for (Users item : usersArrayList) {
            item.setSelected(false);
        }
        newAdapter.notifyDataSetChanged();
    }

    public static void deleteContact(Context context, String contactId) {
        ContentResolver contentResolver = context.getContentResolver();
        Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long.parseLong(contactId));

        contentResolver.delete(ContactsContract.RawContacts.CONTENT_URI, ContactsContract.RawContacts.CONTACT_ID + " = ?", new String[]{String.valueOf(contactId)});

        contentResolver.delete(contactUri, null, null);
    }

    private void getContactList() {
        if (usersArrayList.size() > 0) {
            Comparator<Users> nameComparator = Comparator.comparing(Users::getFirst);
            usersArrayList.sort(nameComparator);

            LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            newAdapter = new NewAdapter(NewContactsFragment.this, usersArrayList, isEdit, button);
            recyclerView.setLayoutManager(manager);
            recyclerView.setAdapter(newAdapter);
        }
    }

    public void intentPass(Users users) {
        Intent intent = new Intent(getActivity(), ContactDetailActivity.class);
        intent.putExtra("user", users);
        launchSomeActivity.launch(intent);
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
        recyclerView = view.findViewById(R.id.show_contact_recyclerview);
        add_contact = view.findViewById(R.id.add_contact);
        edit = view.findViewById(R.id.edit);
        cancel = view.findViewById(R.id.cancel);
        share = view.findViewById(R.id.share);
        delete = view.findViewById(R.id.trash);
        selectall = view.findViewById(R.id.selectall);
        deselectall = view.findViewById(R.id.deselectall);
        create_btn = view.findViewById(R.id.create_contact);
        viewGroup = view.findViewById(android.R.id.content);
        no_contcat_found_linear = view.findViewById(R.id.no_contcat_found_linear);
        Contact_found_linear = view.findViewById(R.id.Contact_found_linear);
        totalcontact = view.findViewById(R.id.totalcontact);
        progressLayout = view.findViewById(R.id.progressLayout);
        searchView = view.findViewById(R.id.search_contact);
        tbmenu = view.findViewById(R.id.tbMenu);
        back = view.findViewById(R.id.back);
//        fastScrollerView = view.findViewById(R.id.fastscroller);
//        fastScrollerThumbView = view.findViewById(R.id.letter_fastscroller_thumb);
    }
}