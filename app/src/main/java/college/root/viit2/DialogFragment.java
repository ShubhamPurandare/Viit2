package college.root.viit2;


import android.app.*;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.*;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class DialogFragment extends android.support.v4.app.DialogFragment {

    View view;


    @NonNull
    @Override
    public android.app.Dialog onCreateDialog(Bundle savedInstanceState) {
        android.support.v7.app.AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
     //  Dialog builder = new Dialog();
        builder.setView(view);

        builder.setPositiveButton("Ok Got it", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
            }
        });


        AlertDialog dialog = builder.create();


        return dialog;

    }

    public DialogFragment() {

    }

    public DialogFragment(View view) {
        // Required empty public constructor

        this.view = view;
    }




}
