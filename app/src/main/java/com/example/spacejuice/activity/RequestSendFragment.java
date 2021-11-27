package com.example.spacejuice.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;


import com.example.spacejuice.MainActivity;
import com.example.spacejuice.R;
import com.example.spacejuice.controller.FollowController;
import com.example.spacejuice.controller.LoginController;

public class RequestSendFragment extends DialogFragment {
    private EditText targetMemberName;
    private Button sendButton;
    private TextView cancel;
    private static final String TAG = "RequestSendFragment";

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.request_fragment, container, false);
        targetMemberName = view.findViewById(R.id.TargetMemberNameRF);
        sendButton = view.findViewById(R.id.requestsButtonRF);
        cancel = view.findViewById(R.id.cancelButtonRF);

        sendButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                if (!targetMemberName.getText().toString().matches("")){
                    Log.d("debug", "being read");
                    FollowController followController = new FollowController();
                    followController.sendRequest(targetMemberName.getText().toString(), new LoginController.OnRequestCompleteCallback() {
                        @Override
                        public void onRequestComplete(boolean suc) {
                            if (suc) {
                                Toast.makeText(getActivity(), "Sent request successfully!", Toast.LENGTH_SHORT).show();
                                getDialog().dismiss();
                            }
                            else {
                                if (targetMemberName.getText().toString().equals(MainActivity.getUser().getMemberName())){
                                    Toast.makeText(getActivity(), "Please don't send follow request to yourself!",
                                            Toast.LENGTH_SHORT).show();
                                    targetMemberName.setText("");
                                }
                                else {
                                    Toast.makeText(getActivity(), "No such user exists!", Toast.LENGTH_SHORT).show();
                                    targetMemberName.setText("");
                                }
                            }
                        }
                    });
                }
                else{
                    Log.d("requestName", "being read");
                    //Toast.makeText(getActivity(), "Must Type a Username", Toast.LENGTH_SHORT).show();
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });


        return view;
    }


}
