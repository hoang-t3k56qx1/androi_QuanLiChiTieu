package android.com.baithuchanh2;


import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.com.baithuchanh2.dal.SQLiteHelper;
import android.com.baithuchanh2.model.Item;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;

public class UpdateDeleteActivity extends AppCompatActivity implements View.OnClickListener{

    private Spinner sp;
    private EditText eTitle, ePrice, eDate;
    private Button btUpdate, btnCancel, btnDelete;
    private Item item;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updatedelete);
        initView();

        item = (Item) getIntent().getSerializableExtra("item");
        eTitle.setText(item.getTitle());
        ePrice.setText(item.getPrice());
        eDate.setText(item.getDate());
        int p = 0;
        for (int i=0; i<sp.getCount(); i++){
            if (sp.getItemAtPosition(i).toString().equals(item.getCategory())){
                p=i;
                break;
            }
        }
        sp.setSelection(p);
        btnCancel.setOnClickListener(this);
        btUpdate.setOnClickListener(this);
        eDate.setOnClickListener(this);
        btnDelete.setOnClickListener(this);


    }

    private void initView() {
        sp = findViewById(R.id.spCategory);
        eTitle = findViewById(R.id.tvTitle);
        ePrice = findViewById(R.id.tvPrice);
        eDate = findViewById(R.id.tvDate);
        btUpdate = findViewById(R.id.btUpdate);
        btnCancel = findViewById(R.id.btCancel);
        btnDelete = findViewById(R.id.btDelete1);
        ArrayAdapter adapter = new ArrayAdapter<>(this,
                R.layout.item_sprinner,
                getResources().getStringArray(R.array.category));
        sp.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        if(v == eDate){
            final Calendar c = Calendar.getInstance();
            int y = c.get(Calendar.YEAR);
            int m = c.get(Calendar.MONTH);
            int d = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
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
                    eDate.setText(date);

                }
            }, y, m, d);
            dialog.show();
        }

        if (v == btnCancel) {
            finish();
        }
        SQLiteHelper db = new SQLiteHelper(this);
        if (v == btUpdate){
            String title = eTitle.getText().toString().trim();
            String gia = ePrice.getText().toString().trim();
            String c= sp.getSelectedItem().toString();
            String date = eDate.getText().toString();
//            Log.d("hoangdev", ""+!title.isEmpty() +" " +gia.matches("\\d+"));
            if(!title.isEmpty() && !date.isEmpty()) {
                Item i = new Item(item.getId(),title, c, gia, date);

                long tb = db.upDate(i);
                if(tb > 0) {
                    Toast.makeText(this, "Sửa thành công", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Có lỗi sảy ra khi update", Toast.LENGTH_SHORT).show();
                }

            } else{
                Toast.makeText(this, "Vui lòng không để trống và đơn vị giá là số!", Toast.LENGTH_SHORT).show();
            }
        }

        if(v == btnDelete) {
            Toast.makeText(this, "aa", Toast.LENGTH_SHORT).show();
            AlertDialog.Builder builder = new android.app.AlertDialog.Builder(UpdateDeleteActivity.this);
            builder.setTitle("Thông báo xóa");
            builder.setMessage("Bạn chắc muốn xóa " +item.getTitle() +" không?");
            builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    long tb = db.delete(item.getId());
                    if(tb > 0) {
                        Toast.makeText(getApplicationContext(), "Xóa thành công", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Có lỗi sảy ra khi xóa", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            builder.show();
        }
    }
}