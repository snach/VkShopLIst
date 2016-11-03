package com.vkshoplist.sfilatov96.vkshoplist;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Created by sfilatov96 on 29.10.16.
 */
public class TitleShopListDialog extends DialogFragment {
    View view;
    private final static String TAG = TitleShopListDialog.class.getSimpleName();


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.shop_list_title_dialog, null);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view)
                // Add action buttons
                .setPositiveButton(R.string.add, null)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        TitleShopListDialog.this.getDialog().cancel();
                        ((CreateListActivty)getActivity()).finish();
                    }
                });
        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        AlertDialog dialog = (AlertDialog) getDialog();
        if (dialog != null) {
            Button positive = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
            //напрямую переопределяем слушателя для кнопки "Отмена" для предотвращения закрытия диалога
            positive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditText title_input = (EditText) view.findViewById(R.id.title_input);

                    if( title_input.getText().toString().isEmpty()){
                        ((CreateListActivty)getActivity()).emptyFields();
                        Log.d(TAG,"created list's title is empty");

                    } else {
                        ((CreateListActivty) getActivity()).GetShopListTitle(title_input.getText().toString());
                        Log.d(TAG,"created list's title: " + title_input.getText().toString());
                        TitleShopListDialog.this.getDialog().dismiss();
                    }
                }
            });
        }
    }
}

