package com.example.app.contacts.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.app.R;
import com.example.app.contacts.adapter.ContactsAdapter;
import com.example.app.contacts.model.ContactItem;

import java.util.ArrayList;
import java.util.List;

/**
 * <h4>ContentProvider 应用之间共享数据</h4>
 *
 * 使用ContentResolver对通信录中的数据进行添加、删除、修改和查询操作。
 * 对联系人进行操作时需要添加权限
 *      1. android.permission.READ_CONTACTS
 *      2. android.permission.WRITE_CONTACTS
 * 联系人相关的uri:
 *      1. ContactsContract.Contacts.CONTENT_URI(content://com.android.contacts/contacts) 操作的数据是联系人信息Uri
 *      2. content://com.android.contacts/data/phones 联系人电话Uri
 *      3. content://com.android.contacts/data/emails 联系人Email Uri
 * 通讯录的类：ContactsContact
 * 三个模型的功能大致为，
 *      1. Data：存储通讯录中每个人的全部信息，什么名字，电话，E-mail等一些乱七八糟和东西全在里面。
 *      2. RawContacts：这个里面好像是说存储的是个人描述信息和一些唯一确定的相关的帐号
 *      3. Contacts：这个好像是通讯录里面的一个人的基本描述，像什么显示的名字，分组情况，有没有电话号码之类的了。
 * app：使用系统的ContentProvider，
 * 打电话、发短信、多语言
 */
public class ContactsActivity extends AppCompatActivity {

    private Button addContact;
    private ListView listView;

    private ContactsAdapter adapter;

    private List<ContactItem> listData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        initView();
        initListener();
    }

    private void initView() {
        addContact = (Button) findViewById(R.id.addContact);
        listView = (ListView) findViewById(R.id.listView);
        adapter = new ContactsAdapter(this, R.layout.item_contact, listData);
        listView.setAdapter(adapter);
    }

    private void initListener() {
        addContact.setOnClickListener(addContackClickListener);
        listView.setOnItemClickListener(onItemClickListener);

    }

    private View.OnClickListener addContackClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showAddContackDialog();
        }
    };

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            showCallSmsDialog(listData.get(position));
        }
    };

    private void getContacts() {
        ContentResolver resolver = getContentResolver();
        Cursor cursor = resolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        while (cursor.moveToNext()) {

            String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

            Cursor phones = resolver.query(Phone.CONTENT_URI, null, Phone.CONTACT_ID + "=" + contactId, null, null);
            while (phones.moveToNext()) {
                String phone = phones.getString(phones.getColumnIndex(Phone.DATA));
                listData.add(new ContactItem(name, phone));
            }
        }
    }

    /**
     * 插入联系人到通讯录
     * */
    private void insertContact(String name, String phone) {
        if (TextUtils.isEmpty(name) && TextUtils.isEmpty(phone)) {
            return;
        }

        ContentResolver resolver = getContentResolver();

        ContentValues values = new ContentValues();
        Uri rawContactUri = getContentResolver().insert(ContactsContract.RawContacts.CONTENT_URI, values);
        long rawContactId = ContentUris.parseId(rawContactUri);

        values.clear();
        values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
        values.put(ContactsContract.Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE);
        values.put(StructuredName.GIVEN_NAME, name);
        resolver.insert(ContactsContract.Data.CONTENT_URI, values);

        values.clear();
        values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
        values.put(ContactsContract.Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE);
        values.put(Phone.NUMBER, phone);
        values.put(Phone.TYPE, Phone.TYPE_MOBILE);
        resolver.insert(ContactsContract.Data.CONTENT_URI, values);
    }

    private void showAddContackDialog() {
        final View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_contact, null);

        final AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(R.string.add_contact)
                .setView(view)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = ((EditText) view.findViewById(R.id.inputName)).getText().toString();
                        String phone = ((EditText) view.findViewById(R.id.inputPhone)).getText().toString();
                        if (TextUtils.isEmpty(name) && TextUtils.isEmpty(phone)) {
                            Toast.makeText(ContactsActivity.this, "请填写姓名和电话号码", Toast.LENGTH_SHORT).show();
                        } else {
                            insertContact(name, phone);
                            getContacts();
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
        builder.create().show();
    }

    private void showCallSmsDialog(final ContactItem contactItem) {
        final View view = LayoutInflater.from(this).inflate(R.layout.dialog_call_sms, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(R.string.please_select)
                .setView(view)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        final AlertDialog dialog =  builder.create();
        dialog.show();

        Button callTel = (Button) view.findViewById(R.id.callTel);
        callTel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 打电话
                // 1. 添加权限
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + contactItem.getPhone()));
                if (ActivityCompat.checkSelfPermission(ContactsActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                startActivity(intent);
                dialog.dismiss();
            }
        });
        Button sendMsg = (Button) view.findViewById(R.id.sendMsg);
        sendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 发短信
                // 1. 添加权限
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.putExtra("sms_body", contactItem.getName() + ":" + contactItem.getPhone());
                intent.setType("vnd.android-dir/mms-sms");
                startActivity(intent);
                dialog.dismiss();
            }
        });

    }

}
