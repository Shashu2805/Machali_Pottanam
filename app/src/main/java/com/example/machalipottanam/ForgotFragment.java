package com.example.machalipottanam;

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
import com.google.firebase.auth.FirebaseAuth;


public class ForgotFragment extends Fragment {



    public ForgotFragment() {
        // Required empty public constructor
    }

    private EditText regemail;
    private Button resetbut;
    private TextView goback;

    private FrameLayout parentLayout;

    private FirebaseAuth firebaseAuth;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_forgot, container, false);

        regemail = view.findViewById(R.id.emailreset);
        resetbut = view.findViewById(R.id.resetbutton_ID);
        goback = view.findViewById(R.id.goback_ID);

        firebaseAuth = FirebaseAuth.getInstance();

        parentLayout = getActivity().findViewById(R.id.frame_lay);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        regemail.addTextChangedListener(new TextWatcher() {
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

        resetbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetbut.setEnabled(false);
                resetbut.setTextColor(Color.argb(50,255,255,255));

                firebaseAuth.sendPasswordResetEmail(regemail.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(getActivity(),"Email Sent Successfully !",Toast.LENGTH_LONG).show();
                                    

                                }else{
                                    String error = task.getException().getMessage();
                                    Toast.makeText(getActivity(),error, Toast.LENGTH_SHORT);
                                }
                                resetbut.setEnabled(true);
                                resetbut.setTextColor(Color.rgb(255,255,255));
                            }
                        });

            }
        });

        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFragment(new SigninFragment());
            }
        });
    }

    private void checkInputs() {
        if(TextUtils.isEmpty(regemail.getText())){
            resetbut.setEnabled(false);
            resetbut.setTextColor(Color.argb(50,255,255,255));

        }else{
            resetbut.setEnabled(true);
            resetbut.setTextColor(Color.rgb(255,255,255));

        }
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(parentLayout.getId(),fragment);
        fragmentTransaction.commit();
    }
}