package au.com.wsit.project11.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import au.com.wsit.project11.R;
import au.com.wsit.project11.adapters.BoardAdapter;
import au.com.wsit.project11.api.ListBoard;
import au.com.wsit.project11.models.Board;

/**
 * Created by guyb on 3/01/17.
 */

public class BoardFragment extends Fragment
{
    private RecyclerView boardRecycler;
    private RecyclerView.LayoutManager layoutManager;
    private BoardAdapter boardAdapter;
    private TextView errorTextView;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_boards, container, false);

        boardRecycler = (RecyclerView) rootView.findViewById(R.id.boardRecyclerView);
        errorTextView = (TextView) rootView.findViewById(R.id.errorTextView);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);

        layoutManager = new LinearLayoutManager(getContext());
        boardRecycler.setLayoutManager(layoutManager);
        boardAdapter = new BoardAdapter(getContext());

        getData();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                getData();
            }
        });

        return rootView;
    }

    private void getData()
    {
        swipeRefreshLayout.setRefreshing(true);
        // Get data
        ListBoard listBoard = new ListBoard();
        listBoard.getBoards(new ListBoard.ListBoardCallback()
        {
            @Override
            public void onSuccess(ArrayList<Board> boards)
            {
                if(boards.size() == 0)
                {
                    showEmptyView();
                }
                else
                {
                    swipeRefreshLayout.setRefreshing(false);
                    errorTextView.setVisibility(View.GONE);
                    boardAdapter.swap(boards);
                }

            }

            @Override
            public void onFail(String result)
            {
                toggleErrorView();
            }
        });

    }

    private void toggleErrorView()
    {
        getActivity().runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                swipeRefreshLayout.setRefreshing(false);
                errorTextView.setVisibility(View.VISIBLE);
                errorTextView.setText(R.string.board_error);
            }
        });
    }

    private void showEmptyView()
    {
        getActivity().runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                swipeRefreshLayout.setRefreshing(false);
                errorTextView.setVisibility(View.VISIBLE);
                errorTextView.setText(R.string.add_boards);
            }
        });
    }
}
