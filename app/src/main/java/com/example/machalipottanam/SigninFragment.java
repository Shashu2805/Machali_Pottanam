package com.example.machalipottanam;

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


public class SigninFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match



    public SigninFragment() {
        // Required empty public constructor
    }
    private TextView donthaveanaccount;
    private FrameLayout parentLayout;

    private TextView forgotpassword;

    private EditText email;
    private EditText password;

    private Button signinBut;

    private FirebaseAuth firebaseAuth;

    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_signin, container, false);
        donthaveanaccount=view.findViewById(R.id.regs_ID);
        parentLayout = getActivity().findViewById(R.id.frame_lay);

        forgotpassword = view.findViewById(R.id.forgot_ID);

        email = view.findViewById(R.id.signin_ID);
        password = view.findViewById(R.id.pass_ID);
        signinBut = view.findViewById(R.id.siginbut_ID);
        firebaseAuth = FirebaseAuth.getInstance();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        donthaveanaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFragment(new SignupFragment());
            }
        });

        forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFragment(new ForgotFragment());
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

        signinBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkEmailandPass();
            }
        });


    }

    private void checkInputs() {
        if(!TextUtils.isEmpty(email.getText().toString())){
            if(!TextUtils.isEmpty(password.getText())){
                signinBut.setEnabled(true);
                signinBut.setTextColor(Color.rgb(255,255,255));
            }else{
                signinBut.setEnabled(false);
                signinBut.setTextColor(Color.argb(50,255, 255, 255 ));
            }
        }else{
            signinBut.setEnabled(false);
            signinBut.setTextColor(Color.argb(50,255, 255, 255 ));
        }
    }

    private void setFragment(Fragment fragment) {
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(parentLayout.getId(),fragment);
                fragmentTransaction.commit();
            }

            private void checkEmailandPass(){
                if(email.getText().toString().matches(emailPattern))
                {
                    if(password.length()>=8)
                    {
                        signinBut.setEnabled(false);
                        signinBut.setTextColor(Color.argb(50,255,255,255));

                        firebaseAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if(task.isSuccessful()){
                                            Intent mainIntent = new Intent(getActivity(),MainImpActivity.class);
                                            startActivity(mainIntent);
                                            getActivity().finish();

                                        }else{
                                            signinBut.setEnabled(true);
                                            signinBut.setTextColor(Color.rgb(255,255,255));

                                            String error = task.getException().getMessage();
                                            Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                });
                    }else
                    {
                        Toast.makeText(getActivity(), "Incorrect Email or Password", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(getActivity(), "Incorrect Email or Password", Toast.LENGTH_SHORT).show();

                }

            }

}


