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
//----------------------------------использ. для измен. и добавлен. контактов
public class MyDialogFragment extends DialogFragment implements View.OnClickListener{

    public final static String DIALOG_LASTNAME = "dialog_lastname";
    public final static String DIALOG_FIRSTNAME = "dialog_firstname";
    public final static String DIALOG_EMAIL = "dialog_email";
//-----------------------------разные ключт для добавлен. и измен.------------------------------
    public final static String DIALOG_METHOD_ADD = "dialog_add";
    public final static String DIALOG_METHOD_UPDATE = "dialog_update";

    private EditText edLastName;
    private EditText edFirstName;
    private EditText edEmail;
    private static String TAG = "===MyDialogFragment===";
    public MainActivity activity;
    private Addable addable;
    private Editable editable;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_edit_fragment, container, false);
        view.findViewById(R.id.btnApply).setOnClickListener(this);
        view.findViewById(R.id.btnCancel).setOnClickListener(this);

        edLastName = view.findViewById(R.id.edtLastName);
        edFirstName = view.findViewById(R.id.edtFirstName);
        edEmail = view.findViewById(R.id.edtEmail);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Bundle bundleArgs = this.getArguments();
        if(bundleArgs.containsKey(MyDialogFragment.DIALOG_LASTNAME)){
            String lastName = bundleArgs.getString(MyDialogFragment.DIALOG_LASTNAME);   //вытащить String-знач. из пакета по ключу-String
            edLastName.setText(lastName);
        }
        if(bundleArgs.containsKey(MyDialogFragment.DIALOG_FIRSTNAME)){
            String firstName = bundleArgs.getString(MyDialogFragment.DIALOG_FIRSTNAME);
            edFirstName.setText(firstName);
        }
        if(bundleArgs.containsKey(MyDialogFragment.DIALOG_EMAIL)){
            String email = bundleArgs.getString(MyDialogFragment.DIALOG_EMAIL);
            edEmail.setText(email);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        addable = (Addable)context;
        editable = (Editable) context;
    }

    @Override
    public void onClick(View v) {   //Update
        switch (v.getId()){
            case R.id.btnApply:                     //забрость в поля Активности знач. из EditText Dialog
                Bundle bundleArgs = MyDialogFragment.this.getArguments();
                activity = (MainActivity) this.getActivity();
                EmailContact contact = new EmailContact();

                if(bundleArgs.containsKey(MyDialogFragment.DIALOG_METHOD_UPDATE)) {
                    if (bundleArgs.containsKey(MyDialogFragment.DIALOG_LASTNAME)) {
//                    activity.curMap.put(MainActivity.ADAPTER_KEY_LASTNAME, edLastName.getText().toString()); //1)-способ a-ля ItStep
                        contact.lastName = edLastName.getText().toString();//2) - "правильный" c исп. интерфейса
                    }
                    if (bundleArgs.containsKey(MyDialogFragment.DIALOG_FIRSTNAME)) {
//                    activity.curMap.put(MainActivity.ADAPTER_KEY_FIRSTNAME, edFirstName.getText().toString());
                        contact.firstName = edFirstName.getText().toString();
                    }
                    if (bundleArgs.containsKey(MyDialogFragment.DIALOG_EMAIL)) {
//                    activity.curMap.put(MainActivity.ADAPTER_KEY_EMAIL, edEmail.getText().toString());
                        contact.email = edEmail.getText().toString();
                    }
                    editable.edit(contact);
                }
                else {
                    if (bundleArgs.containsKey(MyDialogFragment.DIALOG_LASTNAME)) {
                        contact.lastName = edLastName.getText().toString();
                    }
                    if (bundleArgs.containsKey(MyDialogFragment.DIALOG_FIRSTNAME)) {
                        contact.firstName = edFirstName.getText().toString();
                    }
                    if (bundleArgs.containsKey(MyDialogFragment.DIALOG_EMAIL)) {
                        contact.email = edEmail.getText().toString();
                    }
                    addable.add(contact);
                }
//                MainActivity.adapter.notifyDataSetChanged();
                this.dismiss();
                break;
            case R.id.btnCancel:
                this.dismiss();
                break;
        }
    }
}
