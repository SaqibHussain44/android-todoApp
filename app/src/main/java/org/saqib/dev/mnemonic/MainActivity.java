package org.saqib.dev.mnemonic;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class MainActivity extends AppCompatActivity {

    private ArrayList<DropDown_Item> taskList;
    SearchView searchView;
    String selectedCategory ="";
    DBOperation op;
    Spinner spinner;
    EditText quickText;
    int selectedRow = 0;


    @Override
    protected void onStart() {
        spinner = (Spinner) findViewById(R.id.spinner1);
        ArrayList<String> titlesList = new ArrayList<String>();
        titlesList = op.getList();
        if(titlesList == null){
            titlesList = new ArrayList<String>();
        }
        this.taskList = new ArrayList<DropDown_Item>();
        for(int i=0; i<titlesList.size(); i++){
            DropDown_Item item = new DropDown_Item(titlesList.get(i).toString());
            taskList.add(item);
        }
        DropDown_Item item = new DropDown_Item("Finished");
        taskList.add(item);
        DropDown_adapter adapter = new DropDown_adapter(MainActivity.this, R.layout.spinner_text , this.taskList);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        adapter.notifyDataSetChanged();
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                setTaskListView();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showActionBar();
        op = new DBOperation(this);
        final ImageButton submitTaskibtn =(ImageButton)findViewById(R.id.imgSubmit);
        submitTaskibtn.setVisibility(View.GONE);
        quickText = (EditText)findViewById(R.id.quickText);
        quickText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length()>0){
                    submitTaskibtn.setVisibility(View.VISIBLE);
                }
                else{
                    submitTaskibtn.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }


    public void setTaskListView(){
        if(spinner!= null) {
            DropDown_Item item1 = (DropDown_Item) spinner.getSelectedItem();
            selectedCategory = item1.getItemName();
        }
         else{
            selectedCategory = "null";
        }


        final ListAdapter_Task listAdapter;
        ArrayList<ListItem_Task> listItems = op.getAllTasks();
        final ArrayList<ListItem_Task> listToshow = new ArrayList<ListItem_Task>();
        if(selectedCategory.equals("All Lists")){
            for (int i = 0; i < listItems.size(); i++) {
                if (listItems.get(i).getStatus().equals("0")) {
                    listToshow.add(listItems.get(i));
                }
            }
            listAdapter = new ListAdapter_Task(MainActivity.this,listToshow, selectedCategory);
        }
        else {
            for (int i = 0; i < listItems.size(); i++) {
                if (listItems.get(i).getCategory().equals(selectedCategory)) {
                    if(listItems.get(i).getStatus().equals("0")){
                        listToshow.add(listItems.get(i));
                    }
                }
                else if(selectedCategory.equals("Finished")){
                    if(listItems.get(i).getStatus().equals("1")){
                        listToshow.add(listItems.get(i));
                    }
                }
            }
            listAdapter = new ListAdapter_Task(MainActivity.this,listToshow, selectedCategory);
        }

        ListView listView = (ListView)findViewById(R.id.taskList);
        listAdapter.notifyDataSetChanged();
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                ListItem_Task selectedTask = listToshow.get(i);
                Intent intent = new Intent(MainActivity.this,EditTask_Activity.class);
                Bundle b = new Bundle();
                b.putString("title", selectedTask.getTitle());
                b.putString("date", selectedTask.getDate());
                b.putString("time", selectedTask.getTime());
                b.putString("status", selectedTask.getStatus());
                b.putString("category", selectedTask.getCategory());
                intent.putExtras(b);
                startActivity(intent);
            }
        });
    }

    private void showActionBar() {
        LayoutInflater inflator = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.action_bar, null);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowHomeEnabled (false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setCustomView(v);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                if (!searchView.isIconified()) {
                    searchView.setIconified(true);
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                }
                return true;
            case R.id.taskLists:
                Intent intent = new Intent(MainActivity.this,taskCat_list_Activity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.optionsmenu,menu);

        MenuItem item = menu.findItem(R.id.searchmenu);
        searchView =(SearchView) item.getActionView();
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                setTaskListView();
                return false;
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Toast.makeText(MainActivity.this,"searched: "+query,Toast.LENGTH_LONG).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                DBOperation op = new DBOperation(MainActivity.this);
                ArrayList<ListItem_Task> searchlist = op.SearchTasks(newText);
                SetSearchListView(searchlist);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    public void fabClicked(View v){
        Intent intent = new Intent(MainActivity.this,newTask_Activity.class);
        startActivity(intent);
    }

    public void submitQuickTask(View view) {
        op.insertTask("Default",quickText.getText().toString(),"","");
        Toast.makeText(MainActivity.this, "Task added", Toast.LENGTH_SHORT).show();
        quickText.setText("");
        quickText.clearFocus();
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(quickText.getWindowToken(), 0);
        this.onStart();
    }

    private void SetSearchListView(ArrayList<ListItem_Task> list){
        SearchAdapter_Task adapter = new SearchAdapter_Task(this,list);
        ListView lv = (ListView)findViewById(R.id.taskList);
        adapter.notifyDataSetChanged();
        lv.setAdapter(adapter);

    }

    @Override
    public void onBackPressed() {
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        } else {
            super.onBackPressed();
        }
    }


}
