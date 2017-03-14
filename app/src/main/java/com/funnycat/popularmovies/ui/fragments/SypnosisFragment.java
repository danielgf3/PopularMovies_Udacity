package com.funnycat.popularmovies.ui.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.funnycat.popularmovies.R;


public class SypnosisFragment extends Fragment {

    private static final String ARG_ID = "id";
    private static final String ARG_SYPNOSIS = "sypnosis";

    private int mId;
    private String mSypnosis;

    private OnFragmentInteractionListener mListener;

    public SypnosisFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param id Parameter 1.
     * @param sypnosis Parameter 2.
     * @return A new instance of fragment SypnosisFragment.
     */
    public static SypnosisFragment newInstance(int id, String sypnosis) {
        SypnosisFragment fragment = new SypnosisFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_ID, id);
        args.putString(ARG_SYPNOSIS, sypnosis);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mId = getArguments().getInt(ARG_ID);
            mSypnosis = getArguments().getString(ARG_SYPNOSIS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sypnosis, container, false);
        TextView tvSypnosis = (TextView) view.findViewById(R.id.tv_sypnosis);
        tvSypnosis.setText(mSypnosis);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
