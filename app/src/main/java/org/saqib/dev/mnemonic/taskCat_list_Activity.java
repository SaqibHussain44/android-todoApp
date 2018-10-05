package org.saqib.dev.mnemonic;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class taskCat_list_Activity extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_taskslist, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_createtask_Cat:
                final EditText taskEditText = new EditText(this);
                taskEditText.setHint("Enter List Name");
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle("New List ")
                        .setView(taskEditText)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String task = String.valueOf(taskEditText.getText());
                                DBOperation op = new DBOperation(taskCat_list_Activity.this);
                                if (task.equals("")) {
                                    Toast.makeText(taskCat_list_Activity.this, "Fill out input field", Toast.LENGTH_SHORT).show();
                                } else {
                                    if (op.insertTask_Category(task)) {
                                        setListVIew();
                                    }
                                }
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog.show();
                return true;

            case android.R.id.home:
                taskCat_list_Activity.super.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taskcat_list);
        getSupportActionBar().setTitle("Tasks List");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setListVIew();


    }

    private void setListVIew() {
        final ArrayList<ListItem_taskCat> listItems = new ArrayList<ListItem_taskCat>();
        DBOperation op = new DBOperation(this);
        final ArrayList<String> tList = op.getList();
        for (int i = 0; i < tList.size(); i++) {
            listItems.add(new ListItem_taskCat(tList.get(i)));
        }
        listItems.remove(0);
        ListAdapter_taskCat listAdapter = new ListAdapter_taskCat(this, listItems);
        ListView listView = (ListView) findViewById(R.id.ListTaskCat);
        listAdapter.notifyDataSetChanged();
        listView.setAdapter(listAdapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public static Context getContext() {
        return getContext();
    }
}
