package com.example.marketplace24;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class PublicarActivity extends AppCompatActivity {

    private static final String TAG = "PublicarActivity";

    // Vistas
    private ImageView ivImagen;
    private Button btnSeleccionarImagen;
    private TextInputEditText etNombre, etPrecio, etDireccion, etDescripcion;
    private Button btnPublicar;
    private Uri imagenUriSeleccionada;

    private final ActivityResultLauncher<Intent> imagePickerLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                try {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        imagenUriSeleccionada = result.getData().getData();
                        if (imagenUriSeleccionada != null) {
                            ivImagen.setImageURI(imagenUriSeleccionada);
                            Toast.makeText(this, "Imagen seleccionada correctamente.", Toast.LENGTH_SHORT).show();
                        } else {
                            throw new NullPointerException("La URI de la imagen seleccionada es nula.");
                        }
                    } else if (result.getResultCode() == RESULT_CANCELED) {
                        Toast.makeText(this, "Selección de imagen cancelada.", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Error crítico al seleccionar o procesar la imagen: " + e.getMessage(), e);
                    Toast.makeText(this, "Error al cargar la imagen. Por favor, inténtelo de nuevo.", Toast.LENGTH_LONG).show();
                    imagenUriSeleccionada = null;
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publicar);

        // Inicializar vistas
        ivImagen = findViewById(R.id.iv_publicar_imagen);
        btnSeleccionarImagen = findViewById(R.id.btn_seleccionar_imagen);
        etNombre = findViewById(R.id.et_publicar_nombre);
        etPrecio = findViewById(R.id.et_publicar_precio);
        etDireccion = findViewById(R.id.et_publicar_direccion);
        etDescripcion = findViewById(R.id.et_publicar_descripcion);
        btnPublicar = findViewById(R.id.btn_publicar_producto);

        btnSeleccionarImagen.setOnClickListener(v -> {
            try {
                abrirGaleria();
            } catch (Exception e) {
                Log.e(TAG, "Error al intentar abrir la galería: " + e.getMessage());
                Toast.makeText(this, "No se pudo acceder a la galería.", Toast.LENGTH_SHORT).show();
            }
        });

        btnPublicar.setOnClickListener(v -> {
            if (validarCampos()) {
                simularPublicacion();
            }
        });
    }

    private void abrirGaleria() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        imagePickerLauncher.launch(intent);
    }

    private boolean validarCampos() {
        String nombre = etNombre.getText().toString().trim();
        String precio = etPrecio.getText().toString().trim();
        String direccion = etDireccion.getText().toString().trim();
        String descripcion = etDescripcion.getText().toString().trim();

        if (imagenUriSeleccionada == null) {
            Toast.makeText(this, "Debes seleccionar una imagen para el producto.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(nombre)) {
            etNombre.setError("El nombre del producto es obligatorio.");
            return false;
        }

        if (TextUtils.isEmpty(precio) || !precio.matches("^[0-9]+([.,][0-9]{1,2})?$")) {
            etPrecio.setError("Ingresa un precio válido (solo números y opcionalmente dos decimales).");
            return false;
        }

        if (TextUtils.isEmpty(direccion) || direccion.length() < 5) {
            etDireccion.setError("La dirección es obligatoria y debe ser detallada.");
            return false;
        }

        if (TextUtils.isEmpty(descripcion) || descripcion.length() < 10) {
            etDescripcion.setError("La descripción es obligatoria y debe tener al menos 10 caracteres.");
            return false;
        }

        return true;
    }

    private void simularPublicacion() {
        String nombre = etNombre.getText().toString().trim();
        String direccion = etDireccion.getText().toString().trim();

        Toast.makeText(this, "Producto '" + nombre + "' listo. Retiro en: " + direccion, Toast.LENGTH_LONG).show();
        finish();
    }
}
