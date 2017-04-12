package com.oliveboard.testapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Model Class for the added fragment.
 */
public class ModelFragment extends Fragment {

    private TextView mDescriptionView;
    private String mDescriptionText;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_content, container, false);
        mDescriptionView = (TextView)view.findViewById(R.id.description_text);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(null != mDescriptionView) {
            mDescriptionView.setText(mDescriptionText);
        }
    }

    public void setDescriptionText(String descriptionText) {
        mDescriptionText = descriptionText;
    }
}
