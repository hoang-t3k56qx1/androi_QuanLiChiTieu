package android.com.baithuchanh2.dal;

import android.com.baithuchanh2.model.Item;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ChiTieu.db";
    private static int DATABASE_VERSION = 1;

    public SQLiteHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE items("+
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "title TEXT, category TEXT, price TEXT, date TEXT)";
        db.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    // lấy tất cả item sắp xếp theo thời gian giảm dần.
    public List<Item> getAll(){
        List<Item> list = new ArrayList<>();
        SQLiteDatabase st = getReadableDatabase();
        String order = "date DESC";
        Cursor rs = st.query("items",null,
                null, null, null, null, order);

        while (rs != null  && rs.moveToNext()){
            list.add(new Item(rs.getInt(0),
                    rs.getString(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getString(4)));
        }
        return list;
    }
    public long addItem(Item i){
        ContentValues values = new ContentValues();
        values.put("title", i.getTitle());
        values.put("category", i.getCategory());
        values.put("price", i.getPrice());
        values.put("date", i.getDate());
        SQLiteDatabase st = getWritableDatabase();
        return st.insert("items", null, values);
    }

    public List<Item> getItemByDate(String date){
        List<Item> list = new ArrayList<>();
        String whereClause = "date like ?";
        String[]whenArgs = {date};
        SQLiteDatabase st = getReadableDatabase();
        Cursor rs = st.query("items", null, whereClause,
                whenArgs, null, null, null);
        while (rs != null && rs.moveToNext()){
            list.add(new Item(rs.getInt(0),
                    rs.getString(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getString(4)));
        }
        return list;
    }

    public long upDate(Item i){
        ContentValues values = new ContentValues();
        values.put("title", i.getTitle());
        values.put("category", i.getCategory());
        values.put("price", i.getPrice());
        values.put("date", i.getDate());
        SQLiteDatabase st = getWritableDatabase();
        String whereClause = "id = ?";
        String[]whenArgs = {Integer.toString(i.getId())};
        return st.update("items",  values, whereClause, whenArgs);
    }

    public long delete(int id){
        SQLiteDatabase st = getWritableDatabase();
        String whereClause = "id = ?";
        String[]whenArgs = {Integer.toString(id)};
        return st.delete("items", whereClause, whenArgs);
    }


    public List<Item> searchByTitle(String key){
        List<Item> list = new ArrayList<>();
        String whereClause = "title like ?";
        String[]whenArgs = {"%"+key+"%"};
        SQLiteDatabase st = getReadableDatabase();
        Cursor rs = st.query("items", null, whereClause,
                whenArgs, null, null, null);
        while (rs != null && rs.moveToNext()){
            list.add(new Item(rs.getInt(0),
                    rs.getString(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getString(4)));
        }
        return list;
    }


    public List<Item> searchByCategory(String category){
        List<Item> list = new ArrayList<>();
        String whereClause = "category like ?";
        String[]whenArgs = {category};
        SQLiteDatabase st = getReadableDatabase();
        Cursor rs = st.query("items", null, whereClause,
                whenArgs, null, null, null);
        while (rs != null && rs.moveToNext()){
            list.add(new Item(rs.getInt(0),
                    rs.getString(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getString(4)));
        }
        return list;
    }

    public List<Item> searchByDate(String from, String to){
        List<Item> list = new ArrayList<>();
        String whereClause = "date BETWEEN ? AND ?";
        String[]whenArgs = {from.trim(), to.trim()};
        SQLiteDatabase st = getReadableDatabase();
        Cursor rs = st.query("items", null, whereClause,
                whenArgs, null, null, null);
        while (rs != null && rs.moveToNext()){
            list.add(new Item(rs.getInt(0),
                    rs.getString(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getString(4)));
        }
        return list;
    }
}
