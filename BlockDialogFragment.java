package com.syas.jbolt.model.user;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.syas.jbolt.R;

public class BlockDialogFragment extends DialogFragment {

    public static final String BLOCK_ID = "Block Choices";

    @Override
    public Dialog onCreateDialog(Bundle savedInstances) {
        final CharSequence[] choices = getArguments().getCharSequenceArray(BLOCK_ID);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.block_dialog)
                .setItems(choices, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do stuff here.
                    }
                });
        Dialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true);
        return dialog;
    }
}