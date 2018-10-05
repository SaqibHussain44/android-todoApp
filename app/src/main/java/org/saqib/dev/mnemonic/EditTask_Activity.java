package org.saqib.dev.mnemonic;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Saqib on 7/3/2017.
 */

public class EditTask_Activity extends AppCompatActivity {
    private String category;
    private String mydate = " ";
    private String mytime = " ";
    private String taskTitle = "";
    private String taskStatus = "";
    private ArrayList<String> taskList;


    String oldTitle;
    String oldDate;
    String oldTime;
    String oldStatus;
    String oldCategory;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_edittask);
        getSupportActionBar().setTitle("Edit Task");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        oldTitle = bundle.getString("title");
        oldDate = bundle.getString("date");
        oldTime = bundle.getString("time");
        oldStatus = bundle.getString("status");
        oldCategory = bundle.getString("category");


        EditText txtTaskTitle = (EditText) findViewById(R.id.txt_Tasktodo);
        txtTaskTitle.setText(oldTitle);
        CheckBox chkStatus = (CheckBox) findViewById(R.id.checkbox_finishedTask);
        if (oldStatus.equals("1")) {
            chkStatus.setChecked(true);
        } else {
            chkStatus.setChecked(false);
        }

        DBOperation op = new DBOperation(this);
        Spinner spinner = (Spinner) findViewById(R.id.spinnerTaskList);
        this.taskList = op.getList();
        if (taskList.size() == 0 || taskList == null) {
            this.taskList = new ArrayList<String>();
        }
        if (taskList.size() > 1) {
            taskList.remove(0);
        }
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, this.taskList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        int spinnerIndex = 0;
        for (int j = 0; j < taskList.size(); j++) {
            if (oldCategory.equals(taskList.get(j))) {
                spinnerIndex = j;
            }
        }
        spinner.setSelection(spinnerIndex);
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
        editText = (EditText) findViewById(R.id.txt_Taskdate);
        editText.setText(oldDate);
        editText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(EditTask_Activity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        //timepicker

        final EditText Edit_Time = (EditText) findViewById(R.id.time_view_edit);
        Edit_Time.setText(oldTime);
        Edit_Time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(EditTask_Activity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String AM_PM;
                        if (selectedHour < 12) {
                            AM_PM = "AM";
                        } else {
                            AM_PM = "PM";
                        }
                        String minute = String.valueOf(selectedMinute);
                        if (selectedMinute < 10) {
                            minute = String.valueOf(selectedMinute);
                            minute = "0" + minute;
                        }
                        Edit_Time.setText(selectedHour + ":" + minute + " " + AM_PM);
                        mytime = selectedHour + ":" + minute + " " + AM_PM;
                    }
                }, hour, minute, false);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });


        ImageButton btnAddlist = (ImageButton) findViewById(R.id.btnAddnewList);
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
                                DBOperation op = new DBOperation(EditTask_Activity.this);
                                if (op.insertTask_Category(task)) {
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
        EditText editText = (EditText) findViewById(R.id.txt_Taskdate);
        String customDate = "";
        SimpleDateFormat sdfdayofWeek = new SimpleDateFormat("EEE");
        String dayOfTheWeek = sdfdayofWeek.format(myCal.getTime());
        SimpleDateFormat sdfMonth = new SimpleDateFormat("MMM");
        String month = sdfMonth.format(myCal.getTime());
        SimpleDateFormat sdfday = new SimpleDateFormat("dd");
        String day = sdfday.format(myCal.getTime());
        SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");
        String year = sdfYear.format(myCal.getTime());
        customDate = dayOfTheWeek + ", " + month + " " + day + " " + year;
        editText.setText(customDate);
        mydate = customDate;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_newtask, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                EditTask_Activity.super.onBackPressed();
                return true;
            case R.id.action_Done:
                if (mydate == "") {
                    mydate = oldDate;
                }
                if (mytime == "") {
                    mytime = oldTime;
                }

                EditText txtTask = (EditText) findViewById(R.id.txt_Tasktodo);
                taskTitle = txtTask.getText().toString();

                CheckBox chkStatus1 = (CheckBox) findViewById(R.id.checkbox_finishedTask);
                if (chkStatus1.isChecked()) {
                    taskStatus = "1";
                } else {
                    taskStatus = "0";
                }
                if (txtTask.getText().toString().equals("")) {
                    Toast.makeText(this, "Please provide a task Name", Toast.LENGTH_SHORT).show();
                } else {
                    DBOperation op = new DBOperation(this);
                    if (op.UpdateTask(oldCategory, oldTitle, oldDate, oldTime, oldStatus, this.category,
                            this.taskTitle, this.mydate, this.mytime, this.taskStatus)) {
                        Intent intent = new Intent(EditTask_Activity.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(this, "Inserted Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
