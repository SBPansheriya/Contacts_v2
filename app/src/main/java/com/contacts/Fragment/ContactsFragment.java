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
import android.text.TextUtils;
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
import com.contacts.Adapter.HeaderListAdapter;
import com.contacts.Activity.CreateContactActivity;
import com.contacts.Model.Header;
import com.contacts.Model.Users;
import com.contacts.R;
import com.github.ybq.android.spinkit.SpinKitView;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.ThreeBounce;
import com.reddit.indicatorfastscroll.FastScrollItemIndicator;
import com.reddit.indicatorfastscroll.FastScrollerView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class ContactsFragment extends Fragment {

    HeaderListAdapter headerListAdapter;
    RecyclerView recyclerView;
    RelativeLayout tbmenu;
    LinearLayout no_contcat_found_linear, Contact_found_linear;
    ImageView edit, cancel, share, delete, back;
    Button create_btn;
    SearchView searchView;
    TextView selectall, totalcontact, deselectall;
    ImageView add_contact;
    ViewGroup viewGroup;
    Context context;
    Users users;
    SpinKitView spin_kit;
    ActivityResultLauncher<Intent> launchSomeActivity;
    RelativeLayout progressLayout;
    FastScrollerView fastScrollerView;
    ArrayList<Header> headerArrayList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);

        init(view);

        searchView.clearFocus();
        Sprite threeBounce = new ThreeBounce();
        spin_kit.setIndeterminateDrawable(threeBounce);
        progressLayout.setVisibility(View.VISIBLE);

        checkPermission();

        String button = getArguments().getString("btn");

        if (button.equals("fav")) {
            tbmenu.setVisibility(View.VISIBLE);
            edit.setVisibility(View.GONE);
        }
        if (button.equals("no_fav_found")) {
            tbmenu.setVisibility(View.VISIBLE);
            edit.setVisibility(View.GONE);
        }
        if (button.equals("contact")) {
            tbmenu.setVisibility(View.GONE);
        }

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
                        getContactList(false);
                    }
                }
            }
        });

        fastScrollerView.setupWithRecyclerView(
                recyclerView,
                (position) -> {
                    Header header = headerArrayList.get(position);
                    return new FastScrollItemIndicator.Text(
                            header.header.substring(0, 1).toUpperCase()
                    );
                }
        );

        add_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CreateContactActivity.class);
                intent.putExtra("user", users);
                launchSomeActivity.launch(intent);
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
                headerListAdapter.setEdit(true);
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
                headerListAdapter.setEdit(false);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment mFragment = new FavoritesFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, mFragment).setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
            }
        });

        create_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CreateContactActivity.class);
                intent.putExtra("user", users);
                launchSomeActivity.launch(intent);
            }
        });

        if (usersArrayList.isEmpty()) {
            no_contcat_found_linear.setVisibility(View.VISIBLE);
            Contact_found_linear.setVisibility(View.INVISIBLE);
        }

        totalcontact.setText(usersArrayList.size() + " " + "Contacts");

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Users> itemsToRemove = getSelected();

                if (itemsToRemove.isEmpty()) {
                    Toast.makeText(getContext(), "No selected contact found", Toast.LENGTH_SHORT).show();
                } else {
                    Dialog dialog = new Dialog(ContactsFragment.this.getContext());
                    if (dialog.getWindow() != null) {
                        dialog.getWindow().setGravity(Gravity.CENTER);
                        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                        dialog.setCancelable(false);
                    }
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
                            headerListAdapter.setEdit(false);
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

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterContacts(newText);
                return true;
            }
        });
        return view;
    }

    public void filterContacts(String query) {
        query = query.toLowerCase();
        getContactList(true);

        if (!TextUtils.isEmpty(query)) {
            for (Header header : headerArrayList) {
                List<Users> filteredContacts = new ArrayList<>();

                for (Users contact : header.usersList) {
                    if (contact.getFirst().toLowerCase().contains(query) || contact.getLast().toLowerCase().contains(query)) {
                        filteredContacts.add(contact);
                    }
                }
                header.usersList.clear();
                header.usersList.addAll(filteredContacts);
            }

            if (headerArrayList.size() > 0) {
                ArrayList<Header> removeHeaders = new ArrayList<>();
                for (int i = 0; i < headerArrayList.size(); i++) {
                    if (headerArrayList.get(i).usersList.size() == 0) {
                        removeHeaders.add(headerArrayList.get(i));
                    }
                }

                if (removeHeaders.size() > 0) {
                    for (int i = 0; i < removeHeaders.size(); i++) {
                        for (int i1 = 0; i1 < headerArrayList.size(); i1++) {
                            if (removeHeaders.get(i).header.equals(headerArrayList.get(i1).header)) {
                                headerArrayList.remove(i1);
                                break;
                            }
                        }
                    }
                }
            }
        }
        headerListAdapter.setHeaderArrayList(headerArrayList);
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
        getContactList(false);
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
        headerListAdapter.notifyDataSetChanged();
    }

    private void deselectAllItems() {
        for (Users item : usersArrayList) {
            item.setSelected(false);
        }
        headerListAdapter.notifyDataSetChanged();
    }

    public static void deleteContact(Context context, String contactId) {
        ContentResolver contentResolver = context.getContentResolver();
        Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long.parseLong(contactId));

        contentResolver.delete(ContactsContract.RawContacts.CONTENT_URI, ContactsContract.RawContacts.CONTACT_ID + " = ?", new String[]{String.valueOf(contactId)});

        contentResolver.delete(contactUri, null, null);
    }

    private void getContactList(boolean isFilter) {
        /*if (usersArrayList.size() > 0) {
            Comparator<Users> nameComparator = new Comparator<Users>() {
                @Override
                public int compare(Users user1, Users user2) {
                    return user1.getFirst().compareTo(user2.getFirst());
                }
            };
            usersArrayList.sort(nameComparator);
        }

        if (usersArrayList.size() > 0) {
            headerArrayList = new ArrayList<>();
            for (char i = 'A'; i <= 'Z'; i++) {
                Header header = new Header(String.valueOf(i), new ArrayList<>());
                headerArrayList.add(header);
            }
            Header header1 = new Header("#", new ArrayList<>());
            headerArrayList.add(header1);

            for (int i = 0; i < usersArrayList.size(); i++) {
                if (!TextUtils.isEmpty(usersArrayList.get(i).first)) {
                    boolean isMatch = false;
                    for (int i1 = 0; i1 < headerArrayList.size(); i1++) {
                        String header = headerArrayList.get(i1).header;
                        String firstLetter = String.valueOf(usersArrayList.get(i).first.toUpperCase().charAt(0));

                        if (Objects.equals(header, firstLetter)) {
                            isMatch = true;
                            headerArrayList.get(i1).usersList.add(usersArrayList.get(i));
                            break;
                        }
                    }
                    if (!isMatch) {
                        headerArrayList.get(headerArrayList.size() - 1).usersList.add(usersArrayList.get(i));
                    }
                }
            }

            if (headerArrayList.size() > 0) {
                ArrayList<Header> removeHeaders = new ArrayList<>();
                for (int i = 0; i < headerArrayList.size(); i++) {
                    if (headerArrayList.get(i).usersList.size() == 0) {
                        removeHeaders.add(headerArrayList.get(i));
                    }
                }

                if (removeHeaders.size() > 0) {
                    for (int i = 0; i < removeHeaders.size(); i++) {
                        for (int i1 = 0; i1 < headerArrayList.size(); i1++) {
                            if (removeHeaders.get(i).header.equals(headerArrayList.get(i1).header)) {
                                headerArrayList.remove(i1);
                                break;
                            }
                        }
                    }
                }
            }

            if (!isFilter) {
                updateUi();
            }
        }*/

        if (usersArrayList.size() > 0) {
            Comparator<Users> nameComparator = Comparator.comparing(Users::getFirst);
            usersArrayList.sort(nameComparator);

            headerArrayList = new ArrayList<>();
            for (char i = 'A'; i <= 'Z'; i++) {
                Header header = new Header(String.valueOf(i), new ArrayList<>());
                headerArrayList.add(header);
            }
            Header header1 = new Header("#", new ArrayList<>());
            headerArrayList.add(header1);

            usersArrayList.forEach(user -> {
                if (!TextUtils.isEmpty(user.getFirst())) {
                    String firstLetter = String.valueOf(user.getFirst().toUpperCase().charAt(0));
                    boolean isMatch = headerArrayList.stream()
                            .anyMatch(header -> Objects.equals(header.header, firstLetter));

                    if (isMatch) {
                        headerArrayList.stream()
                                .filter(header -> Objects.equals(header.header, firstLetter))
                                .findFirst()
                                .ifPresent(header -> header.usersList.add(user));
                    } else {
                        if (headerArrayList.size() > 0) {
                            headerArrayList.get(headerArrayList.size() - 1).usersList.add(user);
                        }

                    }
                }
            });

            headerArrayList.removeIf(header -> header.usersList.isEmpty());

            if (!isFilter) {
                updateUi(headerArrayList);
            }
        }

        progressLayout.setVisibility(View.GONE);
    }

    private void updateUi(ArrayList<Header> headerArrayList) {
        if (headerListAdapter != null) {
            headerListAdapter.setHeaderArrayList(headerArrayList);
            headerListAdapter.notifyDataSetChanged();
        } else  {
            LinearLayoutManager manager = new LinearLayoutManager(getContext());
            headerListAdapter = new HeaderListAdapter(ContactsFragment.this, headerArrayList);
            recyclerView.setLayoutManager(manager);
            recyclerView.setAdapter(headerListAdapter);
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
            getContactList(false);
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getContactList(false);
            progressLayout.setVisibility(View.GONE);
        } else {
            Toast.makeText(context, "Permission Denied.", Toast.LENGTH_SHORT).show();
            checkPermission();
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
        spin_kit = view.findViewById(R.id.spin_kit);
        searchView = view.findViewById(R.id.search_contact);
        tbmenu = view.findViewById(R.id.tbMenu);
        back = view.findViewById(R.id.back);
        fastScrollerView = view.findViewById(R.id.fastscroller);
    }
}