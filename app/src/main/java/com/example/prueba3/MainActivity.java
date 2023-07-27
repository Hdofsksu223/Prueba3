package com.example.prueba3;

import static com.example.prueba3.AdminBD.DATABASE_NAME;
import static com.example.prueba3.AdminBD.DATABASE_VERSION;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText etCodigo, etDes, etStock, etUbi;
    private Button btnGrabar, btnEliminar;

    private AdminBD adminBD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etCodigo = findViewById(R.id.etCodigo);
        etDes = findViewById(R.id.etDes);
        etStock = findViewById(R.id.etStock);
        etUbi = findViewById(R.id.etUbi);

        btnGrabar = findViewById(R.id.btnGrabar);
        btnEliminar = findViewById(R.id.btnEliminar);

        adminBD = new AdminBD(this, DATABASE_NAME, null, DATABASE_VERSION);

        // Set onClickListener for btnGrabar
        btnGrabar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cod = etCodigo.getText().toString().trim();
                String des = etDes.getText().toString().trim();
                String stockStr = etStock.getText().toString().trim();
                String ubi = etUbi.getText().toString().trim();

                // Validate the stock field input
                int stock = 0;
                if (!stockStr.isEmpty()) {
                    try {
                        stock = Integer.parseInt(stockStr);
                    } catch (NumberFormatException e) {
                        // Handle the case when the input is not a valid integer
                        Toast.makeText(MainActivity.this, "Invalid stock value!", Toast.LENGTH_SHORT).show();
                        return; // Exit the onClick method, preventing further execution
                    }
                } else {
                    // Handle the case when the stock field is empty
                    Toast.makeText(MainActivity.this, "Please enter a stock value!", Toast.LENGTH_SHORT).show();
                    return; // Exit the onClick method, preventing further execution
                }


                // Insert or update the product in the database
                boolean success = adminBD.grabar(cod, des, stock, ubi);

                if (success) {
                    Toast.makeText(MainActivity.this, "Product saved successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Failed to save product!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Set onClickListener for btnEliminar
        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve the 'cod' entered by the user
                String codToDelete = etCodigo.getText().toString().trim();

                // Call the deleteProductByCod method in AdminBD to delete the product
                boolean success = adminBD.deleteProductByCod(codToDelete);

                if (success) {
                    Toast.makeText(MainActivity.this, "Product deleted successfully!", Toast.LENGTH_SHORT).show();
                    clearEditTextFields();
                } else {
                    Toast.makeText(MainActivity.this, "Failed to delete product!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Method to clear EditText fields
    private void clearEditTextFields() {
        etCodigo.setText("");
        etDes.setText("");
        etStock.setText("");
        etUbi.setText("");
    }
}
