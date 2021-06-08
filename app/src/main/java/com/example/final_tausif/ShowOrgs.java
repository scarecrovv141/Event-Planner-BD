package com.example.final_tausif;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class ShowOrgs extends AppCompatActivity {
    RecyclerView mainList;
    FirebaseFirestore fbfs;
    List<com.example.final_tausif.Users> usersList;
    com.example.final_tausif.UsersListAdapter userAdapter;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_orgs);
        this.setTitle("Choose Organizer, Forget Worries");
        usersList=new ArrayList<>();
        mainList=findViewById(R.id.main_list);
        mainList.setHasFixedSize(true);
        mainList.setLayoutManager(new LinearLayoutManager(this));
     //   mainList.setAdapter(userAdapter);
        mAuth = FirebaseAuth.getInstance();

        fbfs= FirebaseFirestore.getInstance();
        userAdapter=new com.example.final_tausif.UsersListAdapter(usersList);
        fbfs.collection("All_Users").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                try{
                for(DocumentChange doc: queryDocumentSnapshots.getDocumentChanges()){
                    if(doc.getType()==DocumentChange.Type.ADDED){
                        if(doc.getDocument().getBoolean("Org")){
                            //All the organisers
                            System.out.println(doc.getDocument().getString("Name"));
                            com.example.final_tausif.Users users=doc.getDocument().toObject(com.example.final_tausif.Users.class);
                            usersList.add(users);
                            userAdapter.notifyDataSetChanged();

                        }
                    }
                }
                }catch(Exception ex){

                }

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        mainList.setAdapter(userAdapter);
        try{
            if(mAuth.getCurrentUser()==null){
                finish();
            }
        }catch(Exception e){
            finish();
        }

    }
}
