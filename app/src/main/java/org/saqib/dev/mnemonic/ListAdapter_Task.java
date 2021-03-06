package org.saqib.dev.mnemonic;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Saqib on 6/16/2017.
 */
public class ListAdapter_Task extends ArrayAdapter<ListItem_Task> {

    private final String category;
    private View ListItemView;
    ArrayList<ListItem_Task> ListItemsArray;

    public ListAdapter_Task(@NonNull Context context, ArrayList<ListItem_Task> ListItemArray, String cat) {
        super(context, 0, ListItemArray);
        this.ListItemsArray = ListItemArray;
        this.category = cat;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable final View convertView, @NonNull final ViewGroup parent) {
        ListItemView = convertView;

        LayoutInflater inflater = LayoutInflater.from(getContext());
        if (ListItemView == null) {
            ListItemView = inflater.inflate(R.layout.list_item_task, parent, false);
        }

        View listview = inflater.inflate(R.layout.list_item_task, parent, false);

        final ListItem_Task currentListItem = getItem(position);

        final CheckBox chk = (CheckBox) ListItemView.findViewById(R.id.checkbox_task);
        chk.setTag(position);
        chk.setText(currentListItem.getTitle());

        final CardView cardview = ListItemView.findViewById(R.id.card_view);

        if (currentListItem.getStatus().equals("1")) {
            chk.setChecked(true);
        }

        final Animation animation = AnimationUtils.loadAnimation(getContext(),
                R.anim.slide_out);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                remove(currentListItem);
                notifyDataSetChanged();
            }
        });

        chk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                DBOperation op = new DBOperation(getContext());
                if (category.equals("Finished")) {
                    op.Negate_CompleteTask(currentListItem.getTitle(), currentListItem.getCategory());
                }
                else {
                    op.CompleteTask(currentListItem.getTitle(), currentListItem.getCategory());
                }

                cardview.startAnimation(animation);

                if(!category.equals("Finished")) {
                    chk.setChecked(false);
                }
                else{
                    chk.setChecked(true);
                }
            }

        });
        TextView titleTextView = (TextView) ListItemView.findViewById(R.id.txtView_taskItem);
        titleTextView.setText(currentListItem.getDate() + " " + currentListItem.getTime());
        return ListItemView;
    }

}
