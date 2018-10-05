package org.saqib.dev.mnemonic;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class newTask_Activity extends AppCompatActivity {
    private String category;
    private String mydate = " ";
    private String mytime = " ";
    private String taskTitle = "";
    private ArrayList<String> taskList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task_);
        getSupportActionBar().setTitle("New Task");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        DBOperation op = new DBOperation(this);
        Spinner spinner = (Spinner) findViewById(R.id.spinnerTaskList);
        this. taskList = op.getList();
        if(taskList.size() == 0 || taskList ==null ){
            this.taskList = new ArrayList<String>();
        }
        if(taskList.size()>1){
            taskList.remove(0);
        }
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, this.taskList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                category = taskList.get(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //datepicker

        final Calendar myCalendar = Calendar.getInstance();
        EditText editText;
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(myCalendar);
            }

        };
        editText = (EditText)findViewById(R.id.txt_Taskdate);
        editText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(newTask_Activity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        //timepicker

        final EditText Edit_Time = (EditText) findViewById(R.id.time_view_edit);
        Edit_Time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(newTask_Activity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String AM_PM ;
                        if(selectedHour < 12) {
                            AM_PM = "AM";
                        } else {
                            AM_PM = "PM";
                        }
                        String minute = String.valueOf(selectedMinute);
                        if(selectedMinute<10){
                            minute = String.valueOf(selectedMinute);
                            minute = "0"+minute;
                        }
                        Edit_Time.setText( selectedHour + ":" + minute+ " "+ AM_PM);
                        mytime = selectedHour + ":" + minute+ " "+ AM_PM;
                    }
                }, hour, minute, false);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });


        ImageButton btnAddlist = (ImageButton)findViewById(R.id.btnAddnewList);
        btnAddlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText taskEditText = new EditText(view.getContext());
                taskEditText.setHint("Enter List Name");
                AlertDialog dialog = new AlertDialog.Builder(view.getContext())
                        .setTitle("New List ")
                        .setView(taskEditText)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String task = String.valueOf(taskEditText.getText());
                                DBOperation op = new DBOperation(newTask_Activity.this);
                                if(op.insertTask_Category(task)) {
                                    taskList.add(task);
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog.show();
            }
        });

    }

    private void updateLabel(Calendar myCal) {
       EditText editText = (EditText)findViewById(R.id.txt_Taskdate);
        String customDate = "";
        SimpleDateFormat sdfdayofWeek = new SimpleDateFormat("EEE");
        String dayOfTheWeek = sdfdayofWeek.format(myCal.getTime());
        SimpleDateFormat sdfMonth = new SimpleDateFormat("MMM");
        String month = sdfMonth.format(myCal.getTime());
        SimpleDateFormat sdfday = new SimpleDateFormat("dd");
        String day = sdfday.format(myCal.getTime());
        SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");
        String year = sdfYear.format(myCal.getTime());
        customDate = dayOfTheWeek + ", "+ month + " "+ day + " " + year;
        editText.setText(customDate);
        mydate = customDate;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_newtask,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle("Are you sure? ")
                        .setMessage("Quit without saving?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                newTask_Activity.super.onBackPressed();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog.show();
                return true;
            case R.id.action_Done:
                EditText txtTask = (EditText)findViewById(R.id.txt_Tasktodo);
                taskTitle = txtTask.getText().toString();
                if(txtTask.getText().toString().equals("")){
                    Toast.makeText(this, "Please provide a task Name", Toast.LENGTH_SHORT).show();
                }
                else{
                    DBOperation op = new DBOperation(this);
                    if(op.insertTask(category,taskTitle,mydate,mytime)){
                        Intent intent = new Intent(newTask_Activity.this, MainActivity.class);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(this, "Inserted Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Confirmation ")
                .setMessage("Are you sure? You want to go back")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        newTask_Activity.super.onBackPressed();
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }
}
