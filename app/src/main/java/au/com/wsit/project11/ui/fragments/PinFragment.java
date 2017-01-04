package au.com.wsit.project11.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import au.com.wsit.project11.R;
import au.com.wsit.project11.adapters.PinAdapter;
import au.com.wsit.project11.models.Pin;
import au.com.wsit.project11.utils.Generator;

/**
 * Created by guyb on 3/01/17.
 */

public class PinFragment extends Fragment
{
    private RecyclerView pinRecycler;
    private RecyclerView.LayoutManager layoutManager;
    private PinAdapter pinAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_pins, container, false);

        pinRecycler = (RecyclerView) rootView.findViewById(R.id.pinFragmentRecycler);

        layoutManager = new GridLayoutManager(getContext(), 2);
        pinRecycler.setLayoutManager(layoutManager);

        pinAdapter = new PinAdapter(getContext());
        pinRecycler.setAdapter(pinAdapter);


        return rootView;
    }

    private void getData()
    {

        setDataInUI(null);

    }

    private void setDataInUI(ArrayList<Pin> pins)
    {
        pinAdapter.swap(pins);
    }
}
