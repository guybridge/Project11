package au.com.wsit.project11.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;

import au.com.wsit.project11.R;
import au.com.wsit.project11.adapters.BoardAdapter;
import au.com.wsit.project11.models.Board;
import au.com.wsit.project11.models.Pin;

/**
 * Created by guyb on 3/01/17.
 */

public class BoardFragment extends Fragment
{
    private RecyclerView boardRecycler;
    private RecyclerView.LayoutManager layoutManager;
    private LinkedHashMap<Board, ArrayList<Pin>> boardData;
    private BoardAdapter boardAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_boards, container, false);

        boardRecycler = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(getContext());
        boardRecycler.setLayoutManager(layoutManager);
        boardAdapter = new BoardAdapter(getContext());


        getData();

        return rootView;
    }

    private void getData()
    {
        // Get data

        loadDataIntoUI(null);
    }

    private void loadDataIntoUI(LinkedHashMap<Board, ArrayList<Pin>> boardData)
    {
        boardAdapter.swap(boardData);
    }
}
