package com.shoppers_era.Activities;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.shoppers_era.R;
import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;
import java.io.ByteArrayOutputStream;
import java.util.UUID;


public class Write_Activity extends AppCompatActivity {


    private static final String IMAGE_URL = "https://apps.itrifid.com/shoppersera/rest_server/diaries/API-KEY/123456";
    private ImageView imageView;
    private static final int CAMERA_PIC_REQUEST =100 ;
    private static final int GALLERY_PIC_REQUEST = 1999;
    @SuppressLint("InlinedApi")
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private Uri image;
    private EditText one_liner;
    private RelativeLayout rel;
    private RelativeLayout relativeLayout;
    private ImageView send_icon;

    //persmission method.
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have read or write permission
        int writePermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int cameraPermission =  ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);
        @SuppressLint("InlinedApi") int readPermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (writePermission != PackageManager.PERMISSION_GRANTED || readPermission != PackageManager.PERMISSION_GRANTED && cameraPermission!= PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);

        verifyStoragePermissions(this);

        rel = (RelativeLayout) findViewById(R.id.rel);

        relativeLayout = (RelativeLayout) findViewById(R.id.relative_layout);

        send_icon = (ImageView) findViewById(R.id.send_img);

        imageView = (ImageView) findViewById(R.id.add_img);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title = "Open Photo";
                CharSequence[] itemlist ={"Take a Photo",
                        "Pick from Gallery",
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(Write_Activity.this);
                //builder.setIcon(R.drawable.icon_app);
                builder.setTitle(title);
                builder.setItems(itemlist, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:// Take Photo
                                cameraIntent();
                                break;
                            case 1:// Choose Existing Photo
                                galleryIntent();
                                break;

                            default:
                                break;
                        }
                    }
                });
                AlertDialog alert = builder.create();
                alert.setCancelable(true);
                alert.show();

            }
        });

        rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = "Open Photo";
                CharSequence[] itemlist ={"Take a Photo",
                        "Pick from Gallery",
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(Write_Activity.this);
                //builder.setIcon(R.drawable.icon_app);
                builder.setTitle(title);
                builder.setItems(itemlist, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:// Take Photo
                                cameraIntent();
                                break;
                            case 1:// Choose Existing Photo
                                galleryIntent();
                                break;

                            default:
                                break;
                        }
                    }
                });
                AlertDialog alert = builder.create();
                alert.setCancelable(true);
                alert.show();

            }
        });

        one_liner = (EditText) findViewById(R.id.message_edit);
        one_liner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.setFocusableInTouchMode(true);
                v.setFocusable(true);
                return false;
            }
        });

        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(image!=null && one_liner.getText().toString() != null){
                    uploadMultipart();
                    finish();
                }else if (image==null){
                    Toast.makeText(Write_Activity.this, "Add an image", Toast.LENGTH_SHORT).show();
                }else if (one_liner.getText().toString() == null){
                    Toast.makeText(Write_Activity.this, "Add your one liner", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void galleryIntent() {

        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, GALLERY_PIC_REQUEST);

    }

    private void cameraIntent() {

        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);

    }



    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);


        ImageView image_view = (ImageView) findViewById(R.id.art_pic);


        if (reqCode == GALLERY_PIC_REQUEST &&  resultCode == RESULT_OK) {

            image = data.getData();

             String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(image,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            image_view.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            image_view.setVisibility(View.VISIBLE);
            rel.setVisibility(View.GONE);

        }else if(reqCode == CAMERA_PIC_REQUEST &&  resultCode == RESULT_OK){

            Bitmap photo = (Bitmap) data.getExtras().get("data");
            image= getImageUri(getApplicationContext(),photo);

            image_view.setImageBitmap(photo);
            image_view.setVisibility(View.VISIBLE);
            rel.setVisibility(View.GONE);

        }else{
            Toast.makeText(this, "Failed to Upload Image", Toast.LENGTH_SHORT).show();
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    //method to get the file path from uri
    public String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }


    public void uploadMultipart() {
        //getting the actual path of the image
        String path = getPath(image);

        //Uploading code
        try {
            String uploadId = UUID.randomUUID().toString();

            //Creating a multi part request
            new MultipartUploadRequest(this, uploadId, IMAGE_URL)
                    .addFileToUpload(path, "user_photo") //Adding file
                    .addParameter("c_id", "27")
                    .addParameter("comment",one_liner.getText().toString())//Adding text parameter to the request
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .startUpload(); //Starting the upload


        } catch (Exception exc) {
            Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
