package com.maximeg.my_french_certificate.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.maximeg.my_french_certificate.R;
import com.maximeg.my_french_certificate.sqlite.QRContract;
import com.maximeg.my_french_certificate.sqlite.QRDbHelper;

import java.io.File;

public class QRActivity extends AppCompatActivity {

    private long itemId;
    private File imgFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);

        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle(R.string.qr_code);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        ImageView imageQR = findViewById(R.id.image_qr);

        itemId = getIntent().getLongExtra(QRContract._ID, -1);

        imgFile = new File(getFilesDir() + "/" + getIntent().getStringExtra(QRContract.COLUMN_NAME_FILE_NAME));

        if(imgFile.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            imageQR.setImageBitmap(myBitmap);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar_menu_qr, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem)
    {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.menu_item:
                new QRDbHelper(getApplicationContext()).deleteQRCode(itemId);
                imgFile.delete();
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }
}
