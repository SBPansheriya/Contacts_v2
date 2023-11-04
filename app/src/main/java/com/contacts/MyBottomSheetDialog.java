package com.contacts;

import static com.contacts.Activity.KeypadScreen.keypadListAdapter;
import static com.contacts.Activity.KeypadScreen.recyclerView;

import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.contacts.Activity.ContactDetailActivity;
import com.contacts.Activity.CreateContactActivity;
import com.contacts.Activity.KeypadScreen;
import com.contacts.Adapter.KeypadListAdapter;
import com.contacts.Class.Constant;
import com.contacts.Model.Users;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

public class MyBottomSheetDialog extends BottomSheetDialogFragment {

    String s;
    EditText editText;


    @SuppressLint("ResourceType")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_dialog_layout, container, false);

        ImageView btn_1 = view.findViewById(R.id.btn_1);
        ImageView btn_2 = view.findViewById(R.id.btn_2);
        ImageView btn_3 = view.findViewById(R.id.btn_3);
        ImageView btn_4 = view.findViewById(R.id.btn_4);
        ImageView btn_5 = view.findViewById(R.id.btn_5);
        ImageView btn_6 = view.findViewById(R.id.btn_6);
        ImageView btn_7 = view.findViewById(R.id.btn_7);
        ImageView btn_8 = view.findViewById(R.id.btn_8);
        ImageView btn_9 = view.findViewById(R.id.btn_9);
        ImageView btn_0 = view.findViewById(R.id.btn_0);
        ImageView btn_hash = view.findViewById(R.id.btn_hash);
        ImageView btn_star = view.findViewById(R.id.btn_star);
        ImageView btn_call = view.findViewById(R.id.btn_call);
        ImageView btn_backpress = view.findViewById(R.id.btn_backpress);
        editText = view.findViewById(R.id.dailer_show);
        TextView add_contact_by_keypad = view.findViewById(R.id.add_contact_by_keypad);

        btn_0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number = String.valueOf(editText.getText());
                s = number + "0";
                editText.setText(s);
            }
        });

        btn_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number = String.valueOf(editText.getText());
                s = number + "1";
                editText.setText(s);
            }
        });

        btn_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number = String.valueOf(editText.getText());
                s = number + "2";
                editText.setText(s);
            }
        });

        btn_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number = String.valueOf(editText.getText());
                s = number + "3";
                editText.setText(s);
            }
        });

        btn_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number = String.valueOf(editText.getText());
                s = number + "4";
                editText.setText(s);
            }
        });

        btn_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number = String.valueOf(editText.getText());
                s = number + "5";
                editText.setText(s);
            }
        });

        btn_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number = String.valueOf(editText.getText());
                s = number + "6";
                editText.setText(s);
            }
        });

        btn_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number = String.valueOf(editText.getText());
                s = number + "7";
                editText.setText(s);
            }
        });

        btn_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number = String.valueOf(editText.getText());
                s = number + "8";
                editText.setText(s);
            }
        });

        btn_9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number = String.valueOf(editText.getText());
                s = number + "9";
                editText.setText(s);
            }
        });

        btn_hash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number = String.valueOf(editText.getText());
                s = number + "#";
                editText.setText(s);
            }
        });

        btn_star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number = String.valueOf(editText.getText());
                s = number + "*";
                editText.setText(s);
            }
        });

        btn_backpress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editText.length() > 0) {
                    String t = s.substring(0, editText.length() - 1);
                    editText.setText("" + t);
                } else {
                    editText.setText("");
                }
            }
        });

        btn_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermissions();
            }
        });

        editText.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s)) {
                    recyclerView.setVisibility(View.GONE);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence query, int start, int before, int count) {

                query = query.toString().toLowerCase();

                ArrayList<Users> filteredList = new ArrayList<>();

                for (int i = 0; i < Constant.usersArrayList.size(); i++) {
                    final String number = Constant.usersArrayList.get(i).getPersonPhone().toLowerCase();
                    if (number.contains(query)) {
                        filteredList.add(Constant.usersArrayList.get(i));
                    }
                }
                if (filteredList.size() > 0) {
                    recyclerView.setVisibility(View.VISIBLE);
                    keypadListAdapter.setFilteredList(filteredList);
                } else {
                    recyclerView.setVisibility(View.GONE);
                }
            }
        });

        add_contact_by_keypad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CreateContactActivity.class);
                intent.putExtra("number", editText.getText().toString());
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        }

        View bottomSheetView = getDialog().findViewById(com.google.android.material.R.id.design_bottom_sheet);
        if (bottomSheetView != null) {
            // Set initial properties (e.g., translate it off the screen)
            bottomSheetView.setTranslationY(1000);

            // Animate the view step by step
            bottomSheetView.animate()
                    .translationY(0)
                    .setDuration(500)
                    .withStartAction(() -> {
                        // Step 1 animation
                    })
                    .withEndAction(() -> {
                        // After the first step animation completes, you can proceed to step 2
                        bottomSheetView.animate()
                                .alpha(1)
                                .setDuration(1000)
                                .withStartAction(() -> {
                                    // Step 2 animation
                                })
                                .start();
                    })
                    .start();
        }

//        if (button.equals("fav")) {
//
//            if (getDialog() != null && getDialog().getWindow() != null) {
//                getDialog().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
//            }
//            View bottomSheetView = getDialog().findViewById(com.google.android.material.R.id.design_bottom_sheet);
//
//            if (bottomSheetView != null) {
//                ObjectAnimator step1Animator = ObjectAnimator.ofFloat(bottomSheetView, "alpha", 0.0f, 1.0f);
//                step1Animator.setDuration(300);
//
//                ObjectAnimator step2Animator = ObjectAnimator.ofFloat(bottomSheetView, "scaleX", 0.5f, 1.0f);
//                ObjectAnimator step2AnimatorY = ObjectAnimator.ofFloat(bottomSheetView, "scaleY", 0.5f, 1.0f);
//                step2Animator.setDuration(300);
//
//                AnimatorSet animatorSet = new AnimatorSet();
//                animatorSet.playSequentially(step1Animator, step2Animator, step2AnimatorY);
//                animatorSet.start();
//            }
//        }
//
//        if (button.equals("recents")) {
//            if (getDialog() != null && getDialog().getWindow() != null) {
//                getDialog().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
//            }
//
//            View bottomSheetView = getDialog().findViewById(com.google.android.material.R.id.design_bottom_sheet);
//
//            if (bottomSheetView != null) {
//                ObjectAnimator step1AnimatorX = ObjectAnimator.ofFloat(bottomSheetView, "scaleX", 0.0f, 1.0f);
//                ObjectAnimator step1AnimatorY = ObjectAnimator.ofFloat(bottomSheetView, "scaleY", 0.0f, 1.0f);
//                ObjectAnimator step1AlphaAnimator = ObjectAnimator.ofFloat(bottomSheetView, "alpha", 0.0f, 1.0f);
//
//                AnimatorSet step1Set = new AnimatorSet();
//                step1Set.playTogether(step1AnimatorX, step1AnimatorY, step1AlphaAnimator);
//                step1Set.setDuration(300);
//
//                ObjectAnimator step2RotateAnimator = ObjectAnimator.ofFloat(bottomSheetView, "rotation", 45.0f, 0.0f);
//                step2RotateAnimator.setDuration(300);
//
//                AnimatorSet animatorSet = new AnimatorSet();
//                animatorSet.playSequentially(step1Set, step2RotateAnimator);
//                animatorSet.start();
//            }
//        }
//
//        if (button.equals("group")){
//
//            if (getDialog() != null && getDialog().getWindow() != null) {
//                getDialog().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
//            }
//
//            View bottomSheetView = getDialog().findViewById(com.google.android.material.R.id.design_bottom_sheet);
//            if (bottomSheetView != null) {
//                // Set initial properties (e.g., translate it off the screen)
//                bottomSheetView.setTranslationY(1000);
//
//                // Animate the view step by step
//                bottomSheetView.animate()
//                        .translationY(0)
//                        .setDuration(500)
//                        .withStartAction(() -> {
//                            // Step 1 animation
//                        })
//                        .withEndAction(() -> {
//                            // After the first step animation completes, you can proceed to step 2
//                            bottomSheetView.animate()
//                                    .alpha(1)
//                                    .setDuration(1000)
//                                    .withStartAction(() -> {
//                                        // Step 2 animation
//                                    })
//                                    .start();
//                        })
//                        .start();
//            }
//
////            View bottomSheetView = getDialog().findViewById(com.google.android.material.R.id.design_bottom_sheet);
////
////            if (bottomSheetView != null) {
////                ObjectAnimator step1AnimatorX = ObjectAnimator.ofFloat(bottomSheetView, "scaleX", 0.5f, 1.0f);
////                ObjectAnimator step1AnimatorY = ObjectAnimator.ofFloat(bottomSheetView, "scaleY", 0.5f, 1.0f);
////
////                ObjectAnimator step2Animator = ObjectAnimator.ofFloat(bottomSheetView, "translationY", 100f, 0f);
////                step2Animator.setDuration(300);
////
////                ObjectAnimator step3AlphaAnimator = ObjectAnimator.ofFloat(bottomSheetView, "alpha", 0.0f, 1.0f);
////
////                AnimatorSet animatorSet = new AnimatorSet();
////                animatorSet.play(step1AnimatorX).with(step1AnimatorY).before(step2Animator);
////                animatorSet.play(step2Animator).before(step3AlphaAnimator);
////                animatorSet.start();
////            }
//        }
    }

    private void call() {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + editText.getText()));
        startActivity(intent);
    }

    private void checkPermissions() {
        String[] permissions = new String[]{Manifest.permission.CALL_PHONE};

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(getActivity(), permissions, 123);
        } else {
            call();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 123 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            call();
        } else if (requestCode == 100 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

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

        gotosettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE) ||
                        shouldShowRequestPermissionRationale(Manifest.permission.READ_PHONE_STATE)) {
                    checkPermissions();
                } else {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                    intent.setData(uri);
                    startActivityForResult(intent, 123);
                    Toast.makeText(getContext(), "Setting", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == 123) {
                checkPermissions();
            }
        } catch (Exception ex) {
            Toast.makeText(getContext(), ex.toString(),
                    Toast.LENGTH_SHORT).show();
        }
    }


//    @Override
//    public void onStart() {
//        super.onStart();
//
//        if (getDialog() != null && getDialog().getWindow() != null) {
//            getDialog().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
//        }
//
//        View bottomSheetView = getDialog().findViewById(com.google.android.material.R.id.design_bottom_sheet);
//
//        if (bottomSheetView != null) {
//            ObjectAnimator step1AnimatorX = ObjectAnimator.ofFloat(bottomSheetView, "scaleX", 0.0f, 1.0f);
//            ObjectAnimator step1AnimatorY = ObjectAnimator.ofFloat(bottomSheetView, "scaleY", 0.0f, 1.0f);
//            ObjectAnimator step1AlphaAnimator = ObjectAnimator.ofFloat(bottomSheetView, "alpha", 0.0f, 1.0f);
//
//            AnimatorSet step1Set = new AnimatorSet();
//            step1Set.playTogether(step1AnimatorX, step1AnimatorY, step1AlphaAnimator);
//            step1Set.setDuration(300);
//
//            ObjectAnimator step2RotateAnimator = ObjectAnimator.ofFloat(bottomSheetView, "rotation", 45.0f, 0.0f);
//            step2RotateAnimator.setDuration(300);
//
//            AnimatorSet animatorSet = new AnimatorSet();
//            animatorSet.playSequentially(step1Set, step2RotateAnimator);
//            animatorSet.start();
//        }
//    }

//    @Override
//    public void onStart() {
//        super.onStart();
//
//        if (getDialog() != null && getDialog().getWindow() != null) {
//            getDialog().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
//        }
//
//        View bottomSheetView = getDialog().findViewById(com.google.android.material.R.id.design_bottom_sheet);
//
//        if (bottomSheetView != null) {
//            ObjectAnimator step1Animator = ObjectAnimator.ofFloat(bottomSheetView, "translationY", 100f, 0f);
//            step1Animator.setDuration(300);
//
//            ObjectAnimator step2AlphaAnimator = ObjectAnimator.ofFloat(bottomSheetView, "alpha", 0.0f, 1.0f);
//            step2AlphaAnimator.setDuration(300);
//
//            AnimatorSet animatorSet = new AnimatorSet();
//            animatorSet.play(step1Animator).before(step2AlphaAnimator);
//            animatorSet.start();
//        }
//    }

}

