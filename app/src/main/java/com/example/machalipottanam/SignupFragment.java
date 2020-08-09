package com.example.machalipottanam;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;


public class SignupFragment extends Fragment {



    public SignupFragment() {
        // Required empty public constructor
    }

    private TextView alreadyhaveacc;
    private FrameLayout parentLayout;

    private EditText uname;
    private EditText email;
    private EditText password;
    private EditText cpassword;

    private Button signup;

    private FirebaseAuth firebaseAuth;

    private FirebaseFirestore firebaseFirestore;

    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_signup, container, false);
        alreadyhaveacc = view.findViewById(R.id.acc_ID);
        parentLayout = getActivity().findViewById(R.id.frame_lay);

        uname = view.findViewById(R.id.user_ID);
        email = view.findViewById(R.id.semail_ID);
        password = view.findViewById(R.id.spass_ID);
        cpassword = view.findViewById(R.id.scpass_ID);

        signup = view.findViewById(R.id.signupbut_ID);

        firebaseAuth = FirebaseAuth.getInstance();

        firebaseFirestore = FirebaseFirestore.getInstance();

        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        alreadyhaveacc.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                setFragment(new SigninFragment());
            }
            });

            uname.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    checkInputs();

                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            email.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    checkInputs();

                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            password.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    checkInputs();

                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            cpassword.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    checkInputs();

                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            signup.setOnClickListener(new OnClickListener(){

                @Override
                public void onClick(View view) {
                    checkEmailandPass();
                }
            });
    }

            private void setFragment(Fragment fragment) {
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(parentLayout.getId(),fragment);
                fragmentTransaction.commit();
            }

            private void checkInputs(){
            if(!TextUtils.isEmpty(uname.getText()))
            {
                if(!TextUtils.isEmpty((email.getText())))
                {
                    if(!TextUtils.isEmpty(password.getText()) && password.length() >= 8)
                    {
                        if(!TextUtils.isEmpty(cpassword.getText()))
                        {
                            signup.setEnabled(true);
                            signup.setTextColor(Color.rgb(255,255,255));
                        }else {
                            signup.setEnabled(false);
                            signup.setTextColor(Color.argb(50,255, 255, 255 ));
                        }

                    }else {
                        signup.setEnabled(false);
                        signup.setTextColor(Color.argb(50,255, 255, 255 ));
                    }

                }else {
                    signup.setEnabled(false);
                    signup.setTextColor(Color.argb(50,255, 255, 255 ));
                }

            }else {
                signup.setEnabled(false);
                signup.setTextColor(Color.argb(50,255, 255, 255 ));
            }

            }

            private void checkEmailandPass(){
                if(email.getText().toString().matches(emailPattern))
                {
                    if(password.getText().toString().equals(cpassword.getText().toString()))
                    {
                        signup.setEnabled(false);
                        signup.setTextColor(Color.argb(50,255,255,255));

                        firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString())
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if(task.isSuccessful()){

                                            HashMap<Object,String> userdata = new HashMap<>();
                                            userdata.put("uname",uname.getText().toString());

                                            firebaseFirestore.collection("USERS")
                                                    .add(userdata)
                                                    .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<DocumentReference> task) {
                                                            if(task.isSuccessful()){
                                                                Intent mainIntent = new Intent(getActivity(),MainImpActivity.class);
                                                                startActivity(mainIntent);
                                                                getActivity().finish();
                                                            }
                                                            else{
                                                                signup.setEnabled(true);
                                                                signup.setTextColor(Color.rgb(255,255,255));
                                                                String error = task.getException().getMessage();
                                                                Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });

                                        }
                                        else{
                                            signup.setEnabled(true);
                                            signup.setTextColor(Color.rgb(255,255,255));
                                            String error = task.getException().getMessage();
                                            Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                });
                    }else {
                        cpassword.setError("Password Mismatch");
                    }
                }else {
                    email.setError("Invalid Email ");
                }

            }
    }
