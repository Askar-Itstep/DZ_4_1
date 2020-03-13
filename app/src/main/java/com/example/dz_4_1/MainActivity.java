package com.example.dz_4_1;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity implements DeletableInterface, Editable, Addable{

    private static int nmrlColor = Color.rgb(0xD3, 0xDF, 0xA4);    //#d3 df a4
    private static int slctColor = Color.rgb(0xE2, 0xA7, 0x6F);
    private static int curPos = -1;
    private static View curView = null;
    private final static String TAG = "===MainActivity===";
    final	static		String	ADAPTER_KEY_LASTNAME	= "adapter_key_lastname";
    final	static		String	ADAPTER_KEY_FIRSTNAME	= "adapter_key_firstname";
    final	static		String	ADAPTER_KEY_EMAIL		= "adapter_key_email";
    private static HashMap<String, String> curMap;  //public - для методов фрагментов
    private ArrayList<HashMap<String, String>> items;
    private static SimpleAdapter adapter;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        items = getItems();  //загруз. списка объектов Contact

        adapter = new SimpleAdapter(this, items, R.layout.email_contact_item, new String[]  {
                                MainActivity.ADAPTER_KEY_LASTNAME,
                                MainActivity.ADAPTER_KEY_FIRSTNAME,
                                MainActivity.ADAPTER_KEY_EMAIL
                        }, new int[] {
                                R.id.tvLastName,
                                R.id.tvFirstName,
                                R.id.tvEmail
                        });

        ListFragment listFragment = new MyListFragment();
        listFragment.setListAdapter(adapter);
        FragmentManager manager	= this.getFragmentManager();
        FragmentTransaction transaction	= manager.beginTransaction();

        transaction.add(R.id.flFragmentContainer, listFragment);
        transaction.commit();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private ArrayList<HashMap<String, String>> getItems() {
        // ----- Коллекция с данными -------------------------------------------
        ArrayList<EmailContact> emails = new ArrayList<>();
        emails.add(new EmailContact("White", "Steven", "steven@gmail.com"));
        emails.add(new EmailContact("Black", "Bill", "bill@microsoft.com"));
        emails.add(new EmailContact("Smith", "Mary", "mary_smith@gmail.com"));
        emails.add(new EmailContact("Green", "Peter", "green_p@meta.com"));
        emails.add(new EmailContact("Peach", "Sara", "sara@microsoft.com"));
        emails.add(new EmailContact("Gray", "Serg", "grayserg@gmail.com"));
        emails.add(new EmailContact("Lemon", "Kristina", "unknown@meta.com"));

        items = new ArrayList<>();

// ----список items -стопка карточек  -----------------------------------------------
        for (int i = 0; i < emails.size(); i++) {
            HashMap<String, String> fieldMap = new HashMap<>(); //текущ. карточка
            EmailContact contact = emails.get(i);
            fieldMap.put(MainActivity.ADAPTER_KEY_LASTNAME, contact.lastName);
            fieldMap.put(MainActivity.ADAPTER_KEY_FIRSTNAME, contact.firstName);
            fieldMap.put(MainActivity.ADAPTER_KEY_EMAIL, contact.email);

            items.add(fieldMap);
        }
//        items.forEach(i->Log.d(TAG, i.get(MainActivity.ADAPTER_KEY_LASTNAME)));   //!
        return items;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = this.getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    public DeletableInterface deletable;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        MyDialogFragment dialog = new MyDialogFragment();
        Bundle bundleArgs = new Bundle();

        switch (id){
            case R.id.action_add: {
//                HashMap<String, String> newMap = new HashMap<>();     //сначала добав. пуст. карточку
//                newMap.put(MainActivity.ADAPTER_KEY_LASTNAME, "");
//                newMap.put(MainActivity.ADAPTER_KEY_FIRSTNAME, "");
//                newMap.put(MainActivity.ADAPTER_KEY_EMAIL, "");
//                items.add(newMap);
//                curMap = newMap;
//                this.sendBundle(dialog, bundleArgs);

                bundleArgs.putString(MyDialogFragment.DIALOG_FIRSTNAME, ""); //2)-зачем карточку, пусть фрагмент работает
                bundleArgs.putString(MyDialogFragment.DIALOG_LASTNAME, "");
                bundleArgs.putString(MyDialogFragment.DIALOG_EMAIL, "");

                bundleArgs.putString(MyDialogFragment.DIALOG_METHOD_ADD, "add");
                dialog.setArguments(bundleArgs);   //передача данных в окно
                dialog.show(getFragmentManager(), "dialog Adding");
            }
                break;

            case R.id.action_delete: {
                if (curPos == -1) {
                    Toast.makeText(this, "Выберите сотрудника", Toast.LENGTH_SHORT).show();
                    break;
                }
                String name = curMap.get(MainActivity.ADAPTER_KEY_FIRSTNAME);
                Log.d(TAG, "curName: " + name);

                MyDialogFragmentDelete dialogDel = new MyDialogFragmentDelete();
                bundleArgs.putString(MyDialogFragmentDelete.DIALOG_FIRSTNAME, curMap.get(MainActivity.ADAPTER_KEY_FIRSTNAME));
                bundleArgs.putString(MyDialogFragmentDelete.DIALOG_LASTNAME, curMap.get(MainActivity.ADAPTER_KEY_LASTNAME));
                bundleArgs.putString(MyDialogFragmentDelete.DIALOG_EMAIL, curMap.get(MainActivity.ADAPTER_KEY_EMAIL));

                dialogDel.setArguments(bundleArgs);   //передача данных в окно
                dialogDel.show(getFragmentManager(), "dialog Delete");
//                adapter.notifyDataSetChanged();
            }
                break;

            case R.id.action_update: {
                if (curPos == -1) {
                    Toast.makeText(this, "Выберите сотрудника", Toast.LENGTH_SHORT).show();
                    break;
                }
                bundleArgs.putString(MyDialogFragment.DIALOG_METHOD_UPDATE, "update");
                sendBundle(dialog, bundleArgs);
//                adapter.notifyDataSetChanged();
            }
                break;
        }
        return true;
    }
    //---------------------------передача данных в MyDialogFragment--------------------------
    private void sendBundle(MyDialogFragment dialog, Bundle bundleArgs) {
        bundleArgs.putString(MyDialogFragment.DIALOG_FIRSTNAME, curMap.get(MainActivity.ADAPTER_KEY_FIRSTNAME));
        bundleArgs.putString(MyDialogFragment.DIALOG_LASTNAME, curMap.get(MainActivity.ADAPTER_KEY_LASTNAME));
        bundleArgs.putString(MyDialogFragment.DIALOG_EMAIL, curMap.get(MainActivity.ADAPTER_KEY_EMAIL));
        dialog.setArguments(bundleArgs);
        dialog.show(getFragmentManager(), "dialog");
    }
    @Override
//    public void remove(HashMap mContact) { //1)способ - ч/з доступ к curMap во фрагменте
//        items.remove(mContact);
    public void remove(EmailContact contact){
//        items.remove(curMap);       //2) простой способ с curMap без фрагмента
        Iterator<HashMap<String, String>> iter = items.iterator();  //3)правильный
        while (iter.hasNext()){
            HashMap<String, String> item = iter.next();
            if(item.get(MainActivity.ADAPTER_KEY_LASTNAME).equals(contact.lastName) && item.get(MainActivity.ADAPTER_KEY_FIRSTNAME)
                    .equals(contact.firstName) && item.get(MainActivity.ADAPTER_KEY_EMAIL).equals(contact.email)){
                iter.remove();
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
//    public void add(HashMap mContact) {
//        items.add(mContact);
    public void add(EmailContact contact) {
        HashMap<String, String> mContact = new HashMap<String, String>();
        mContact.put(MainActivity.ADAPTER_KEY_LASTNAME, contact.lastName);
        mContact.put(MainActivity.ADAPTER_KEY_FIRSTNAME, contact.firstName);
        mContact.put(MainActivity.ADAPTER_KEY_EMAIL, contact.email);
        items.add(mContact);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void edit(EmailContact contact) {
        curMap.put(MainActivity.ADAPTER_KEY_LASTNAME, contact.lastName);
        curMap.put(MainActivity.ADAPTER_KEY_FIRSTNAME, contact.firstName);
        curMap.put(MainActivity.ADAPTER_KEY_EMAIL, contact.email);
        adapter.notifyDataSetChanged();
    }

    //анон. класс примера изм. по треб. системы (была ошибка стар. верс.)
    public static class MyListFragment extends ListFragment {
        @Override
        public void onResume() {
            super.onResume();
            //вытащить ListView + handler
            this.getListView().setOnItemClickListener(new MyOnItemClickListener());
        }
    }
    private static class MyOnItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            curMap = (HashMap<String, String>)parent.getAdapter().getItem(position);
            String	str	= "Last Name  : " + curMap.get(MainActivity.ADAPTER_KEY_LASTNAME)  + "\n" +
                    "First Name : "	+ curMap.get(MainActivity.ADAPTER_KEY_FIRSTNAME) + "\n" +
                    "Email      : "	+ curMap.get(MainActivity.ADAPTER_KEY_EMAIL);

            if(curPos != -1)
                curView.setBackgroundColor(nmrlColor);
            curPos = position;
            curView = view;
            curView.setBackgroundColor(slctColor);

            Toast.makeText(parent.getContext(), str, Toast.LENGTH_SHORT).show();    //!!!!!!
        }
    }
}
