package android.com.baithuchanh2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.com.baithuchanh2.dal.SQLiteHelper;
import android.com.baithuchanh2.model.Item;
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

public class AddActivity extends AppCompatActivity implements View.OnClickListener{

    private Spinner sp;
    private EditText eTitle, ePrice, eDate;
    private Button btUpdate, btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        initView();
        btnCancel.setOnClickListener(this);
        btUpdate.setOnClickListener(this);
        eDate.setOnClickListener(this);
    }

    private void initView() {
        sp = findViewById(R.id.spCategory);
        eTitle = findViewById(R.id.tvTitle);
        ePrice = findViewById(R.id.tvPrice);
        eDate = findViewById(R.id.tvDate);
        btUpdate = findViewById(R.id.btUpdate);
        btnCancel = findViewById(R.id.btCancel);
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

        if (v == btUpdate){
            String title = eTitle.getText().toString().trim();
            String gia = ePrice.getText().toString().trim();
            String c= sp.getSelectedItem().toString();
            String date = eDate.getText().toString();
            Log.d("hoangdev", ""+!title.isEmpty() +" " +gia.matches("\\d+"));
            if(!title.isEmpty() && !date.isEmpty()) {
                Item i = new Item(title, c, gia, date);
                SQLiteHelper db = new SQLiteHelper(this);
                long tb = db.addItem(i);
                if(tb > 0) {
                    Toast.makeText(this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Có lỗi sảy ra khi thêm vui lòng thêm lại", Toast.LENGTH_SHORT).show();
                }

            } else{
                Toast.makeText(this, "Vui lòng không để trống và đơn vị giá là số!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}