package com.example.prueba3;

import static com.example.prueba3.AdminBD.DATABASE_NAME;
import static com.example.prueba3.AdminBD.DATABASE_VERSION;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class BuscarProducto extends AppCompatActivity {

    private EditText etBuscar;
    private Button btnBsc;
    private ListView listView;
    private AdminBD adminBD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_producto);

        etBuscar = findViewById(R.id.etBuscar);
        btnBsc = findViewById(R.id.btnBsc);
        listView = findViewById(R.id.listView);

        adminBD = new AdminBD(this, DATABASE_NAME, null, DATABASE_VERSION);

        btnBsc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Perform search in the database and populate the ListView
                String searchQuery = etBuscar.getText().toString();

                // Query the database using AdminBD class and get matching products
                List<String> matchingProducts = queryProductsFromDatabase(searchQuery);

                // Populate the ListView with the matching products
                ArrayAdapter<String> adapter = new ArrayAdapter<>(BuscarProducto.this,
                        android.R.layout.simple_list_item_1, matchingProducts);
                listView.setAdapter(adapter);
            }
        });

        // Handle item click in the ListView
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Return to MainActivity and fill EditText fields with the selected product's data
                String selectedProduct = (String) parent.getItemAtPosition(position);

                // Parse the selected product data and fill the EditText fields
                String[] productData = selectedProduct.split(": ");
                String cod = productData[0];
                String des = productData[1];
                String stock = productData[2];
                String ubi = productData[3];

                EditText etCodigo = findViewById(R.id.etCodigo);
                EditText etDes = findViewById(R.id.etDes);
                EditText etStock = findViewById(R.id.etStock);
                EditText etUbi = findViewById(R.id.etUbi);

                etCodigo.setText(cod);
                etDes.setText(des);
                etStock.setText(stock);
                etUbi.setText(ubi);
            }
        });
    }

    private List<String> queryProductsFromDatabase(String searchQuery) {
        List<String> matchingProducts = new ArrayList<>();

        // Open the database in read mode
        SQLiteDatabase db = adminBD.getReadableDatabase();

        // Define the columns to retrieve from the table
        String[] projection = {"cod", "des", "stock", "ubi"};

        // Define the WHERE clause and the arguments for the query
        String selection = "cod LIKE ? OR des LIKE ?";
        String[] selectionArgs = new String[]{"%" + searchQuery + "%", "%" + searchQuery + "%"};

        // Execute the query
        Cursor cursor = db.query("productos", projection, selection, selectionArgs, null, null, null);

        // Iterate through the cursor and add matching products to the list
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String cod = cursor.getString(cursor.getColumnIndexOrThrow("cod"));
                String des = cursor.getString(cursor.getColumnIndexOrThrow("des"));
                int stock = cursor.getInt(cursor.getColumnIndexOrThrow("stock"));
                String ubi = cursor.getString(cursor.getColumnIndexOrThrow("ubi"));

                String productEntry = cod + ": " + des + ": " + stock + ": " + ubi;
                matchingProducts.add(productEntry);
            }
            cursor.close();
        }

        db.close();
        return matchingProducts;
    }
}
