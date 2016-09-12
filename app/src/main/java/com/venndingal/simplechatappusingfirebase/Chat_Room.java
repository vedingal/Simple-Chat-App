package com.venndingal.simplechatappusingfirebase;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by keng on 12/09/16.
 */
public class Chat_Room extends AppCompatActivity {

    private Button btn_send;
    private EditText et_message;
    private TextView tv_chatConversation;

    private String userName, roomName;

    private DatabaseReference root;
    private String tempKey;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        btn_send = (Button) findViewById(R.id.btn_send);
        et_message = (EditText) findViewById(R.id.et_message);
        tv_chatConversation = (TextView) findViewById(R.id.tv_chatConversation);

        userName = getIntent().getExtras().getString("userName").toString();
        roomName = getIntent().getExtras().getString("roomName").toLowerCase();

        setTitle("Room - " + roomName);

        root = FirebaseDatabase.getInstance().getReference().child(roomName);

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> map = new HashMap<String, Object>();
                tempKey = root.push().getKey();
                root.updateChildren(map);

                DatabaseReference message_root = root.child(tempKey);
                Map<String, Object> map2 = new HashMap<String, Object>();
                map2.put("name", userName);
                map2.put("msg", et_message.getText().toString());
                message_root.updateChildren(map2);
                et_message.setText("");


            }
        });

        root.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                appendChatConversation(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                appendChatConversation(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private String chatMsg, chatUserName;
    private void appendChatConversation(DataSnapshot dataSnapshot) {

        Iterator i = dataSnapshot.getChildren().iterator();
        while (i.hasNext()){
            chatMsg = (String) ((DataSnapshot) i.next()).getValue();
            chatUserName = (String) ((DataSnapshot) i.next()).getValue();
            tv_chatConversation.append(chatUserName + " : " + chatMsg + "\n");
        }
    }
}
