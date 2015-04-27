package com.syas.jbolt.model.user;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.syas.jbolt.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MatchingStatusDialogFragment extends DialogFragment {

    public static final String MATCHING_STATUS_ID = "Matching Status Choices";
    public static final String INITIAL_MATCHING_STATUS_ID = "Initial Matching Status";

    private CharSequence[] choices;
    private int selectedPos;

    public interface MatchingStatusDialogOnClickListener {
        public void onClick(DialogFragment dialog, int which);
    }

    private MatchingStatusDialogOnClickListener mListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstances) {
        choices = getArguments().getCharSequenceArray(MATCHING_STATUS_ID);
        String initialChoice = getArguments().getString(INITIAL_MATCHING_STATUS_ID);

        if(savedInstances != null){
            selectedPos = savedInstances.getInt("selected");
        }else {
            selectedPos = -1;
            for (int i = 0; i < choices.length; i++) {
                if (choices[i].equals(initialChoice)) {
                    selectedPos = i;
                    break;
                }
            }
        }

        MatchingStatusDialogAdapter adapter = new MatchingStatusDialogAdapter();
        ListView listView = new ListView(getActivity());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(adapter);
        /*FontTextView title = new FontTextView(getActivity());
        title.setGravity(Gravity.CENTER);
        title.setText(R.string.matching_status_dialog);
        title.setTextSize(25f);
        title.setPadding(0, 40, 0, 40);*/
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.matching_status_dialog)
                .setView(listView)
                .setPositiveButton(R.string.error_dialog_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.e(null, "here1");
                        mListener = (MatchingStatusDialogOnClickListener) getActivity();
                        if (mListener != null) {
                            Log.e(null, "here2");
                            mListener.onClick(MatchingStatusDialogFragment.this, selectedPos);
                        }
                    }
                })
                .setNegativeButton(R.string.error_dialog_cancel, null);
        Dialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true);
        return dialog;
    }


    @Override
    public void onSaveInstanceState(Bundle out){
        super.onSaveInstanceState(out);
        if(selectedPos > -1){
            out.putInt("selected",selectedPos);
        }
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (MatchingStatusDialogOnClickListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement DatingStatusDialogListener");
        }
    }

    private class MatchingStatusDialogAdapter extends BaseAdapter implements AdapterView.OnItemClickListener {

        @Override
        public int getCount() {
            return choices.length;
        }

        @Override
        public Object getItem(int position) {
            return choices[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, android.view.View convertView, ViewGroup parent) {
            MatchingStatusDialogViewHolder holder;

            if (convertView == null) {
                convertView = LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.multi_choice_item, parent, false);
                holder = new MatchingStatusDialogViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (MatchingStatusDialogViewHolder) convertView.getTag();
            }

            if (selectedPos == position) {
                holder.checked.setImageResource(R.drawable.check);
                convertView.setBackgroundColor(Color.argb(255, 242, 242, 242));
            } else {
                holder.checked.setImageResource(R.drawable.not_checked);
                convertView.setBackgroundColor(Color.WHITE);
            }

            holder.choice.setText((String) choices[position]);
            return convertView;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectedPos = position;
            notifyDataSetChanged();
        }
    }

    static class MatchingStatusDialogViewHolder {
        @InjectView(R.id.checked)
        ImageView checked;

        @InjectView(R.id.choice)
        TextView choice;

        MatchingStatusDialogViewHolder(View v) {
            ButterKnife.inject(this, v);
        }
    }
}