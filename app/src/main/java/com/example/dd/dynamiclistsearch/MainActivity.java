package com.example.dd.dynamiclistsearch;

import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DataAdapter.RecyclerListener {

    private EditText etSearch;
    private RecyclerView recData;

    private DataAdapter dataAdapter;
    private List<DataModel> dummyData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etSearch = findViewById(R.id.etSearch);
        recData = findViewById(R.id.recData);

        loadDummyData();

        //Set layout manager
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recData.setLayoutManager(llm);

        // Item Divider
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recData.addItemDecoration(dividerItemDecoration);

        dataAdapter = new DataAdapter(dummyData);
        dataAdapter.setRecyclerListener(this);
        recData.setAdapter(dataAdapter);

        searchListener();
    }

    private void searchListener() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                dataAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void loadDummyData() {
        dummyData = new ArrayList<>();
        dummyData.add(new DataModel("ABC Enterprise","9123456789"));
        dummyData.add(new DataModel("XYZ Enterprise","9123645458"));
        dummyData.add(new DataModel("XYZ Trading Co.","5979798456"));
        dummyData.add(new DataModel("DRD Trading Co.","6124165498"));
        dummyData.add(new DataModel("Akshay Tradelink","7564165498"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_items, menu);
        MenuItem searchViewItem = menu.findItem(R.id.action_search);
        final SearchView searchViewAndroidActionBar = (SearchView) MenuItemCompat.getActionView(searchViewItem);
        searchViewAndroidActionBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchViewAndroidActionBar.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                dataAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }



    @Override
    public void onItemClick(View view, int position, DataModel dataModel) {
        String msg = "Item Removed at " + position + "\nName: " + dataModel.getTitle();
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
}
