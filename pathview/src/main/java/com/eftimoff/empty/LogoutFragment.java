package com.eftimoff.empty;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;

import com.eftimoff.androipathview.PathView;

public class LogoutFragment extends Fragment {

    Button btn;

    PathView pathView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_logout, container, false);
        btn = (Button) view.findViewById(R.id.btn);
        pathView = (PathView) view.findViewById(R.id.pathView);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pathView.getPathAnimator().delay(100).
                        duration(1500).
                        interpolator(new AccelerateDecelerateInterpolator()).
                        start();
            }
        });


        return view;
    }

}