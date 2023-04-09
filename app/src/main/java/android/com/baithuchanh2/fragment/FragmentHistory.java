package android.com.baithuchanh2.fragment;

import android.com.baithuchanh2.MainActivity;
import android.com.baithuchanh2.R;
import android.com.baithuchanh2.UpdateDeleteActivity;
import android.com.baithuchanh2.adapter.RecycleViewAdapter;
import android.com.baithuchanh2.dal.SQLiteHelper;
import android.com.baithuchanh2.model.Item;
import android.com.baithuchanh2.model.ItemListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FragmentHistory extends Fragment implements ItemListener {
    private RecyclerView rcView;
    private RecycleViewAdapter adapter;
    private SQLiteHelper db;

    private List<Item> list;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_history, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rcView = view.findViewById(R.id.rcViewHistory);
        adapter = new RecycleViewAdapter();
        db = new SQLiteHelper(getContext());
        list = db.getAll();
//        list.add(new Item(1, "A", "b", "c", "dgdg"));
        adapter.setList(list);
        LinearLayoutManager manager = new LinearLayoutManager(getContext(),
                RecyclerView.VERTICAL, false);
        rcView.setLayoutManager(manager);
        rcView.setAdapter(adapter);
        adapter.setItemListener(this);
    }

    @Override
    public void onItemClick(View view, int position) {
        Item item = adapter.getItem(position);
        Intent intent = new Intent(getActivity(), UpdateDeleteActivity.class);
        intent.putExtra("item", item);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        list = db.getAll();
        for (Item i : list) {
            Log.d("hoangdev", i.getTitle());
        }
        adapter.setList(list);
    }
}
