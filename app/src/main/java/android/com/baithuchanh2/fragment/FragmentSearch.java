package android.com.baithuchanh2.fragment;

import android.app.DatePickerDialog;
import android.com.baithuchanh2.R;
import android.com.baithuchanh2.adapter.RecycleViewAdapter;
import android.com.baithuchanh2.dal.SQLiteHelper;
import android.com.baithuchanh2.model.Item;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FragmentSearch extends Fragment implements View.OnClickListener{

    private RecyclerView rcView;
    private TextView tvTong;
    private SearchView seacrch;
    private Button btnSearch;
    private EditText eFrom, eTo;
    private Spinner sp;
    private RecycleViewAdapter adapter;
    private SQLiteHelper db;
    List<Item> list = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        adapter = new RecycleViewAdapter();
        db = new SQLiteHelper(getContext());
        list = db.getAll();
        adapter.setList(list);
        tvTong.setText("Tổng: " +tinhTong() + " K");
        LinearLayoutManager manager = new LinearLayoutManager(getContext(),
                RecyclerView.VERTICAL, false);
        rcView.setLayoutManager(manager);
        rcView.setAdapter(adapter);


        seacrch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                list = db.searchByTitle(s);
                adapter.setList(list);
                tvTong.setText("Tổng: " +tinhTong() + " K");
                Toast.makeText(getContext(), ""+s, Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        eFrom.setOnClickListener(this);
        eTo.setOnClickListener(this);
        btnSearch.setOnClickListener(this);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String c = sp.getItemAtPosition(position).toString();
                if(!c.equalsIgnoreCase("all")) {
                    list = db.searchByCategory(c);
                } else {
                    list = db.getAll();
                }
                adapter.setList(list);
                tvTong.setText("Tổng: " +tinhTong() + " K");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private long tinhTong(){
        long tong =0;
        for(Item i : list){
            tong += Long.parseLong(i.getPrice());
        }
        return tong;
    }

    private void initView(View v) {
        rcView = v.findViewById(R.id.rcViewSearch);
        tvTong =  v.findViewById(R.id.tvTong);
        seacrch =  v.findViewById(R.id.search);
        btnSearch =  v.findViewById(R.id.btSearch);
        eFrom =  v.findViewById(R.id.eFrom);
        eTo =  v.findViewById(R.id.eTo);
        sp =  v.findViewById(R.id.spCategory);
        String[] arr = getResources().getStringArray(R.array.category);
        String[] arr1 = new String[arr.length+1];
        arr1[0] ="All";
        for (int i = 0; i < arr.length; i++) {
            arr1[i+1] = arr[i];
        }
        sp.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.item_sprinner, arr1));
    }

    @Override
    public void onClick(View v) {
        if(v == eFrom) {
            final Calendar c = Calendar.getInstance();
            int y = c.get(Calendar.YEAR);
            int m = c.get(Calendar.MONTH);
            int d = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int y, int m, int d) {
                    String date ="";
                    if(m>8){
                        date = d+"/"+(m+1)+"/"+y;
                    } else {
                        date = d+"/0"+(m+1)+"/"+y;
                    }
                    if (d < 10) {
                        date = "0"+date;
                    }
                    eFrom.setText(date);

                }
            }, y, m, d);
            dialog.show();
        }


        if(v == eTo) {
            final Calendar c = Calendar.getInstance();
            int y = c.get(Calendar.YEAR);
            int m = c.get(Calendar.MONTH);
            int d = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int y, int m, int d) {
                    String date ="";
                    if(m>8){
                        date = d+"/"+(m+1)+"/"+y;
                    } else {
                        date = d+"/0"+(m+1)+"/"+y;
                    }
                    if (d < 10) {
                        date = "0"+date;
                    }
                    eTo.setText(date);

                }
            }, y, m, d);
            dialog.show();
        }


        if(v == btnSearch) {
            String from = eFrom.getText().toString();
            String to = eTo.getText().toString();
            Log.e("hoangdev", "search " + from+" "+ to);
            if(from.isEmpty() || to.isEmpty()) {
                Toast.makeText(getContext(), "Vui lòng không để trống", Toast.LENGTH_SHORT).show();
            } else {
                list = db.searchByDate(from, to);
            }
            adapter.setList(list);
            tvTong.setText("Tổng: " +tinhTong() + " K");
        }
    }


}
