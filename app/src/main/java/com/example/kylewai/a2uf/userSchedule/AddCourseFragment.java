package com.example.kylewai.a2uf.userSchedule;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.kylewai.a2uf.R;

public class AddCourseFragment extends Fragment
{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_add_class, container);
        createCloseButtonListener(view);
        return view;
    }

    public void createCloseButtonListener(View inflatedView)
    {
        Button closeButton = inflatedView.findViewById(R.id.addClass_close_button);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }
}
