package soy.dow.nbang.nbangtravel;

import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DiaryCreateActivity extends AppCompatActivity implements DatePickerFragment.OnCompleteListener {

    private SQLiteDatabase db = null;
    public static int checks = 0;
    public static int getEditIntent = 0;
    public static int editDiary = 0;
    static final int GET_GOOD_PIC = 1;
    private final int GALLERY_CODE=1112;
    public static int editId;
    private String pictureImagePath = "";
    public static int EDIT_ID;
    private static Cursor constantsCursor = null;
    public static int goback = 0;


    public void onComplete(String date) {
        TextView textView = (TextView) findViewById(soy.dow.nbang.nbangtravel.R.id.diary_create_date);
        textView.setText(date);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        db = (new DataBaseHelper(this)).getWritableDatabase();
        super.onCreate(savedInstanceState);

        getSupportActionBar().setTitle("다이어리");
        setContentView(soy.dow.nbang.nbangtravel.R.layout.activity_diary_create);

        if(checks == 1){
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = timeStamp + ".jpg";
            File storageDir = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES);
            pictureImagePath = storageDir.getAbsolutePath() + "/" + imageFileName;
            File file = new File(pictureImagePath);
            Uri outputFileUri = FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".fileprovider", file);
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            cameraIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            if(cameraIntent.resolveActivity(getPackageManager())!= null) {
                startActivityForResult(cameraIntent, GET_GOOD_PIC);
            }
            checks = 0;
        }

        if(getEditIntent ==1){
            Intent intent = getIntent();
            EDIT_ID = intent.getExtras().getInt("edit_ID");
            constantsCursor = db.rawQuery("SELECT " + "*" +
                    " FROM " + DiaryContract.ConstantEntry.TABLE_NAME +
                    " WHERE " + DiaryContract.ConstantEntry._ID + " = " + EDIT_ID, null);
            Log.i("------I DID NOT GET ONE", constantsCursor.getCount()+"");
            if(constantsCursor.getCount() == 1){
                Log.i("---------I GOT ONE", constantsCursor.getCount()+"");
                if(constantsCursor.moveToFirst()) {
                    TextView title = (TextView) findViewById(soy.dow.nbang.nbangtravel.R.id.diary_create_title);
                    title.setText(constantsCursor.getString(2));
                    TextView date = (TextView) findViewById(soy.dow.nbang.nbangtravel.R.id.diary_create_date);
                    date.setText(constantsCursor.getString(1));
                    TextView content = (TextView) findViewById(soy.dow.nbang.nbangtravel.R.id.diary_create_content);
                    content.setText(constantsCursor.getString(4));
                    ImageView picture = (ImageView) findViewById(soy.dow.nbang.nbangtravel.R.id.diary_create_picture);
                    byte[] src = constantsCursor.getBlob(3);
                    Bitmap b = null;
                    if (src != null) {
                        b = BitmapFactory.decodeByteArray(src, 0, src.length);
                    }
                    picture.setImageBitmap(b);
                }
            }
            editDiary = 1;
            getEditIntent = 0;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(soy.dow.nbang.nbangtravel.R.menu.diary_create_actionbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case soy.dow.nbang.nbangtravel.R.id.diary_create_bar_album:
                selectGallery();
                return true;
            case soy.dow.nbang.nbangtravel.R.id.diary_create_bar_camera:
                onClickcamera();
                return true;
            case soy.dow.nbang.nbangtravel.R.id.diary_create_bar_save:
                try {
                    save();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void selectGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, GALLERY_CODE);
    }

    public void onClickcamera(){
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = timeStamp + ".jpg";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        pictureImagePath = storageDir.getAbsolutePath() + "/" + imageFileName;
        File file = new File(pictureImagePath);
        Uri outputFileUri = FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".fileprovider", file);
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        cameraIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        if(cameraIntent.resolveActivity(getPackageManager())!= null) {
            startActivityForResult(cameraIntent, GET_GOOD_PIC);
        }
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            // Do the file write
            if (requestCode == 1) {
                File imgFile = new File(pictureImagePath);
                try {
                    ExifInterface exif = null;
                    exif = new ExifInterface(imgFile.getAbsolutePath());
                    int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

                    imgFile.createNewFile();
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                    Bitmap bmRotated = rotateBitmap(myBitmap, orientation);

                    ImageView frame = (ImageView) findViewById(soy.dow.nbang.nbangtravel.R.id.diary_create_picture);
                    frame.setImageBitmap(bmRotated);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            // Request permission from the user
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }
        //갤러리에서 가져오기
        if (requestCode == GALLERY_CODE && resultCode == RESULT_OK) {
            InputStream in = null;
            try {
                Uri uri = data.getData();
                in = getContentResolver().openInputStream(uri);
                Bitmap imageBitmap = BitmapFactory.decodeStream(in);

                ExifInterface exif = null;
                exif = new ExifInterface(getPath(uri));
                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
                Bitmap bmRotated = rotateBitmap(imageBitmap, orientation);

                ((ImageView) findViewById(soy.dow.nbang.nbangtravel.R.id.diary_create_picture)).setImageBitmap(bmRotated);
                in.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        startManagingCursor(cursor);
        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(columnIndex);
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, int orientation) {
        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_NORMAL:
                return bitmap;
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.setScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.setRotate(180);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                matrix.setRotate(90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.setRotate(-90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(-90);
                break;
            default:
                return bitmap;
        }
        try {
            Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap.recycle();
            return bmRotated;
        }
        catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 0:
                if (requestCode == 1) {
                    File imgFile = new File(pictureImagePath);
                    try {
                        Log.i("FILEPATH-------------", pictureImagePath);
                        imgFile.createNewFile();
                        Log.i("HERE----------------", "IMAGE FILE EXISTS");
                        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                        ((ImageView) findViewById(soy.dow.nbang.nbangtravel.R.id.diary_create_picture)).setImageBitmap(myBitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        }
    }

    public void save() throws IOException {
        ImageView imageView = (ImageView)findViewById(soy.dow.nbang.nbangtravel.R.id.diary_create_picture);
        Bitmap resized;
        byte[] barray = null;
        if(TextUtils.isEmpty(((TextView)findViewById(soy.dow.nbang.nbangtravel.R.id.diary_create_date)).getText())){
            Toast.makeText(this, "날짜를 반드시 입력해주세요", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(((EditText)findViewById(soy.dow.nbang.nbangtravel.R.id.diary_create_title)).getText())){
            Toast.makeText(this, "다이어리의 제목을 반드시 입력해주세요", Toast.LENGTH_SHORT).show();
        }else if((BitmapDrawable)imageView.getDrawable() == null) {
            Toast.makeText(this, "다이어리엔 사진이 필수!", Toast.LENGTH_SHORT).show();
        }else {
            resized = getResizedBitmap(((BitmapDrawable)imageView.getDrawable()).getBitmap(), imageView.getDrawable().getMinimumWidth());
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            resized.compress(Bitmap.CompressFormat.JPEG, 50, stream);
            barray = stream.toByteArray();
            resized.recycle();
            stream.close();

            ContentValues values = new ContentValues();
            values.put(DiaryContract.ConstantEntry.COLUMN_NAME_DATE, ((TextView)findViewById(soy.dow.nbang.nbangtravel.R.id.diary_create_date)).getText().toString());
            values.put(DiaryContract.ConstantEntry.COLUMN_NAME_TITLE, ((EditText)findViewById(soy.dow.nbang.nbangtravel.R.id.diary_create_title)).getText().toString());
            values.put(DiaryContract.ConstantEntry.COLUMN_NAME_PICTURE, barray);
            values.put(DiaryContract.ConstantEntry.COLUMN_NAME_CONTENT, ((EditText)findViewById(soy.dow.nbang.nbangtravel.R.id.diary_create_content)).getText().toString());
            values.put(DiaryContract.ConstantEntry.COLUMN_NAME_TRAVEL, DataBaseHelper.now_travel);
            if(editDiary == 1) {
                db.update(DiaryContract.ConstantEntry.TABLE_NAME, values,"_id = "+ editId, null);
                Toast.makeText(this, "수정되었습니다", Toast.LENGTH_SHORT).show();
                editDiary = 0;
            } else {
                db.insert(DiaryContract.ConstantEntry.TABLE_NAME, DiaryContract.ConstantEntry.COLUMN_NAME_DATE, values);
                Toast.makeText(this, "저장되었습니다", Toast.LENGTH_SHORT).show();
            }
            Intent intent = new Intent(this, MainActivity.class);
            MainActivity.check_ac=88;
            startActivity(intent);
            finish();
        }
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();
        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    @Override
    public void onBackPressed() {
        if (goback==1) {
            super.onBackPressed();
            goback = 0;
        }else{
            new AlertDialog.Builder(this).setTitle("다이어리 작성을 취소할까요?").setPositiveButton("네", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    goback=1;
                    onBackPressed();
                }
            }).setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    //ignore
                }
            }).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (constantsCursor !=null ){
            constantsCursor.close();
        }
        if(db != null) {
            db.close();
        }
    }
}
