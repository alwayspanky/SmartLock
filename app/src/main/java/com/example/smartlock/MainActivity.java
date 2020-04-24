package com.example.smartlock;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private Button lock, unlock, Logout;
    Blockchain blockchain;
    Block block;
    String LockId = "", BlockId = "";
    String UserId;

    DatabaseReference databaseReference;
    DatabaseReference blockchainDatabaseReference;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Logout = findViewById(R.id.btnLogout);
        lock  = findViewById(R.id.btnLock);
        unlock = findViewById(R.id.btnUnlock);
        mAuth = FirebaseAuth.getInstance();

        UserId = FirebaseAuth.getInstance().getCurrentUser().getUid();


        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
                finish();
            }
        });

        lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                databaseReference = FirebaseDatabase.getInstance().getReference("Lock-value");
                //Adding Lockvalue
                LockId = databaseReference.push().getKey();
                databaseReference.child(LockId).setValue(1);
                AddBlock();

            }
        });

        unlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference = FirebaseDatabase.getInstance().getReference("Lock-value");
                //Adding Lockvalue
                LockId = databaseReference.push().getKey();
                databaseReference.child(LockId).setValue(0);
                AddBlock();

            }
        });
    }


    public void AddBlock(){
        blockchainDatabaseReference = FirebaseDatabase.getInstance().getReference("Blockchain");
        GetBlockChain();

    }

    public void GetBlockChain(){
        blockchain = new Blockchain(4);

        blockchainDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    int a = 1;
                    String data = snapshot.child("data").getValue(String.class);
                    String hash = snapshot.child("hash").getValue(String.class);
                    int index = snapshot.child("index").getValue(int.class);
                    long timestamp = snapshot.child("timestamp").getValue(long.class);
                    if(blockchain.blocks.size() == 0){
                        block = new Block(index, timestamp, null, data);
                    }
                    else {
                        block = new Block(index, timestamp, blockchain.latestBlock().getHash(), data);
                    }

                    block.mineBlock(4);
                    blockchain.blocks.add(block);
                }
                AddBlockNow();



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("The read failed: " ,databaseError.getMessage());
            }

        });
    }

    public void AddBlockNow(){
        blockchain.addBlock(blockchain.newBlock(LockId));
        block = blockchain.latestBlock();
        BlockId = blockchainDatabaseReference.push().getKey();
        blockchainDatabaseReference.child(BlockId).setValue(block);

        Toast.makeText(MainActivity.this, "Value Added", Toast.LENGTH_SHORT).show();


    }

}
