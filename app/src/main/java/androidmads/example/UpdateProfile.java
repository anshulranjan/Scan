package androidmads.example;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UpdateProfile extends AppCompatActivity {
    EditText edittextName, address1;
    Button b1,b2;
    Spinner getcovid, getuses;
    DatabaseReference databaseRegistrations;
    String name, address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        databaseRegistrations = FirebaseDatabase.getInstance().getReference("registrations");
        b1 = (Button) findViewById(R.id.adddetails);
        b1.setVisibility(View.INVISIBLE);
        b2 =(Button) findViewById(R.id.back);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UpdateProfile.this, MainActivity.class));
            }
        });
        edittextName = (EditText) findViewById(R.id.editText11);
        address1 = (EditText) findViewById(R.id.editText12);
        getcovid = (Spinner) findViewById(R.id.spinner);
        getuses = (Spinner) findViewById(R.id.spinner1);
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        String number = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
        DatabaseReference userNameRef = rootRef.child("registrations").child(number);
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                name = dataSnapshot.child("name").getValue(String.class);
                address = dataSnapshot.child("address").getValue(String.class);
                edittextName.setText(name);
                address1.setText(address);
                b1.setVisibility(View.VISIBLE);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("Error", databaseError.getMessage()); //Don't ignore errors!
            }
        };
        userNameRef.addListenerForSingleValueEvent(eventListener);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRegsitration();
            }
        });
    }
    private void addRegsitration()
    {
        String name = edittextName.getText().toString().trim();
        String address = address1.getText().toString().trim();
        String suff = getcovid.getSelectedItem().toString();
        String usesinfo = getuses.getSelectedItem().toString();
        if (!TextUtils.isEmpty(name)) {
            String number = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
            Details details = new Details(name, address, number, suff, usesinfo);
            databaseRegistrations.child(number).setValue(details);
            Toast.makeText(this, "Your Profile has been updated", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(UpdateProfile.this, UpdateProfile.class));
        }else
        {
            Toast.makeText(this, "Please enter a name", Toast.LENGTH_LONG).show();
        }
    }
}
