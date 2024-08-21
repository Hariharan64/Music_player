package com.example.qradmin;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

public class AdminActivity extends AppCompatActivity {

    private EditText etEmployeeName, etEmployeeNo, etEmployeeDesignation, etEmployeePhone;
    private Button btnAddEmployee;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize UI elements
        etEmployeeName = findViewById(R.id.et_employee_name);
        etEmployeeNo = findViewById(R.id.et_employee_no);
        etEmployeeDesignation = findViewById(R.id.et_employee_designation);
        etEmployeePhone = findViewById(R.id.et_employee_phone);
        btnAddEmployee = findViewById(R.id.btn_add_employee);

        // Set onClickListener for the Add Employee button
        btnAddEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addEmployee();
            }
        });
    }

    private void addEmployee() {
        String name = etEmployeeName.getText().toString().trim();
        String number = etEmployeeNo.getText().toString().trim();
        String designation = etEmployeeDesignation.getText().toString().trim();
        String phone = etEmployeePhone.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(number) ||
                TextUtils.isEmpty(designation) || TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create an Employee object
        Employee employee = new Employee(name, number, designation, phone);

        // Use the employee number as the unique ID for Firestore document
        db.collection("employees")
                .document(phone)  // Use the employee number as the document ID
                .set(employee)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(AdminActivity.this, "Employee added with Phone: " + phone, Toast.LENGTH_SHORT).show();
                    // Clear the fields or finish the activity
                    etEmployeeName.setText("");
                    etEmployeeNo.setText("");
                    etEmployeeDesignation.setText("");
                    etEmployeePhone.setText("");
                })
                .addOnFailureListener(e -> Toast.makeText(AdminActivity.this, "Failed to add employee", Toast.LENGTH_SHORT).show());
    }
}
