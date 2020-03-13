package com.example.dz_4_1;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

public class MyDialogFragmentDelete extends DialogFragment implements View.OnClickListener{

    public final static String DIALOG_LASTNAME = "dialog_lastname";
    public final static String DIALOG_FIRSTNAME = "dialog_firstname";
    public final static String DIALOG_EMAIL = "dialog_email";

    private TextView tvLastName;
    private TextView tvFirstName;
    private TextView tvEmail;
    private static String TAG = "===MyDialogFragmentDelete===";
    private DeletableInterface deletable;
    private static String lastName, firstName, email;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_delete_fragment, container, false);
        view.findViewById(R.id.btnApply).setOnClickListener(this);
        view.findViewById(R.id.btnCancel).setOnClickListener(this);

        tvLastName = view.findViewById(R.id.tvDelLastName);Log.d(TAG, "lastName: "+tvLastName.getText());
        tvFirstName = view.findViewById(R.id.tvDelFirstName);
        tvEmail = view.findViewById(R.id.tvDelEmail);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Bundle bundleArgs = this.getArguments();
        if(bundleArgs.containsKey(MyDialogFragmentDelete.DIALOG_LASTNAME)){
            lastName = bundleArgs.getString(MyDialogFragmentDelete.DIALOG_LASTNAME);   //вытащить String-знач. из пакета по ключу-String
            tvLastName.setText(lastName);
            Log.d(TAG, "lastName: "+lastName);
        }
        if(bundleArgs.containsKey(MyDialogFragmentDelete.DIALOG_FIRSTNAME)){
            firstName = bundleArgs.getString(MyDialogFragmentDelete.DIALOG_FIRSTNAME);
            tvFirstName.setText(firstName);
        }
        if(bundleArgs.containsKey(MyDialogFragmentDelete.DIALOG_EMAIL)){
            email = bundleArgs.getString(MyDialogFragmentDelete.DIALOG_EMAIL);
            tvEmail.setText(email);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        deletable = (DeletableInterface) context;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnApply:                     //забрость в поля Активности знач. из EditText Dialog
//                Bundle bundleArgs = MyDialogFragmentDelete.this.getArguments();
//                String name = MainActivity.curMap.get(MainActivity.ADAPTER_KEY_FIRSTNAME);
                //1)c доступом к полю MainActivity
//                deletable.remove(MainActivity.curMap);

                //2) "правильно"
                EmailContact contact = new EmailContact(lastName, firstName, email);
                deletable.remove(contact);

                this.dismiss();
                break;
            case R.id.btnCancel:
                this.dismiss();
                break;
        }
    }
}
