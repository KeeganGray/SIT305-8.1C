package com.example.a81c;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.a81c.model.Playlist;

import java.util.ArrayList;
import java.util.List;

public class MyPlaylist extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private ListView listview;

    public MyPlaylist() {
        // Required empty public constructor
    }

    public static MyPlaylist newInstance(String param1, String param2) {
        MyPlaylist fragment = new MyPlaylist();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.playlist_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listview = view.findViewById(R.id.listview);
        listViewManage();
    }

    public void listViewManage(){

        List<Playlist> table = ((MainActivity)getActivity()).playlistGet();
        List<String> URLs = new ArrayList<>();
        for (int i =0 ; i < table.size(); i++){
            Playlist x = table.get(i);
            URLs.add(x.URL);
        }

        ArrayAdapter<String> listViewAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, URLs);
        listview.setAdapter(listViewAdapter);
    }
}