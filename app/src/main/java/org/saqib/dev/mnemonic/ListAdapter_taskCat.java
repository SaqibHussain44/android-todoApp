package org.saqib.dev.mnemonic;

import android.content.Context;
import android.content.DialogInterface;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Saqib on 4/22/2017.
 */

public class ListAdapter_taskCat extends ArrayAdapter<ListItem_taskCat> {
    private Context context;

    public ListAdapter_taskCat(@NonNull Context context, ArrayList<ListItem_taskCat> ListItmeDetail) {
        super(context, 0, ListItmeDetail);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View ListItemView = convertView;

        LayoutInflater inflater = LayoutInflater.from(getContext());
        if(ListItemView==null){
            ListItemView = inflater.inflate(R.layout.taskcat_listitem,parent,false);
        }

        final ListItem_taskCat currentListItem = getItem(position);

        final TextView titleTextView = (TextView) ListItemView.findViewById(R.id.title);
        titleTextView.setText(currentListItem.getItemName());
        ImageButton btnDelete = (ImageButton)ListItemView.findViewById(R.id.deletebtn);
        ImageButton btnEdit = (ImageButton)ListItemView.findViewById(R.id.editbtn);
        btnDelete.setImageResource(R.drawable.ic_delete_blue_24dp);
        btnEdit.setImageResource(R.drawable.ic_mode_edit_blue_24dp);


        if(currentListItem.getItemName().equals("Default")){

            btnDelete.setVisibility(View.INVISIBLE);
            btnEdit.setVisibility(View.INVISIBLE);
        }
        else{

            btnDelete = (ImageButton)ListItemView.findViewById(R.id.deletebtn);
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog dialog = new AlertDialog.Builder(context)
                            .setTitle("Confirmation ")
                            .setMessage("Are you sure? All tasks of this category will also get deleted")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    DBOperation op = new DBOperation(context);
                                    op.deleteTaskCategory(currentListItem.getItemName());
                                    remove(currentListItem);
                                    notifyDataSetChanged();
                                }
                            })
                            .setNegativeButton("Cancel", null)
                            .create();
                    dialog.show();
                }
            });

            btnEdit = (ImageButton)ListItemView.findViewById(R.id.editbtn);
            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Edit List");
                    final EditText input = new EditText(context);
                    input.setText(currentListItem.getItemName());
                    input.setSelection(input.getText().length());
                    builder.setView(input);
                    builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            DBOperation op = new DBOperation(context);
                            op.UpdateTaskCategory(input.getText().toString(),currentListItem.getItemName());
                            currentListItem.setItemName(input.getText().toString());
                            notifyDataSetChanged();
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();

                }
            });
        }
        return ListItemView;
    }

}
