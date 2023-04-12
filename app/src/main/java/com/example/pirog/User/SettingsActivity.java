package com.example.pirog.User;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pirog.Model.MainAdp;
import com.example.pirog.Prevalent.Prevalent;
import com.example.pirog.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;
import io.paperdb.Paper;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class SettingsActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks{

    private RecyclerView profileImageView;
    private EditText fullNameEditText, userPhoneEditText, addressEditText, userPassEditTxt;
    private TextView saveTextButton,  closeTextButton, addTextButton;

    private StorageReference storageProfilePictureRef;
    private StorageTask uploadTask;

    private Uri imageUri;
    private String checker = "";

    ArrayList <Uri> arrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        profileImageView = (RecyclerView) findViewById(R.id.settings_account_image);
        fullNameEditText = (EditText) findViewById(R.id.settings_fullname);
        addressEditText = (EditText) findViewById(R.id.settings_address);
        saveTextButton = (TextView) findViewById(R.id.save_settings_tv);
        closeTextButton = (TextView) findViewById(R.id.close_settings_tv);
        addTextButton = (TextView) findViewById(R.id.add_btn_image);
        userPassEditTxt = (EditText) findViewById(R.id.settings_password);
        storageProfilePictureRef = FirebaseStorage.getInstance().getReference().child("Profile pictures");

        userInfoDisplay(profileImageView, fullNameEditText, userPhoneEditText,addressEditText);

        closeTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginIntent = new Intent(SettingsActivity.this, UsersHome.class);
                startActivity(loginIntent);
            }
        });

        saveTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if (checker.equals("clicked"))
                {
                    userInfoSaved();
                }
                else
                {
                    updateOnlyUserInfo();
                }
            }
        });

        addTextButton.setOnClickListener(view ->{
            String[] strings = {Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE};

            if (EasyPermissions.hasPermissions(this,strings)){
                imagePicker();
            }else{
                EasyPermissions.requestPermissions(
                        this,
                        "App needs access to your camera & storage",
                        100,
                        strings
                );
            }
        });
    }

    private void updateOnlyUserInfo() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");

        HashMap<String, Object> userMap = new HashMap<>();
        userMap. put("name", fullNameEditText.getText().toString());
        userMap. put("address", addressEditText.getText().toString());
        userMap. put("password", userPassEditTxt.getText().toString());
        ref.child(Prevalent.currentOnlineUser.getPhone()).updateChildren(userMap);

        startActivity(new Intent(SettingsActivity.this, UsersHome.class));
        Toast.makeText(SettingsActivity.this, "Успешно сохранено", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void userInfoSaved() {
        if (TextUtils.isEmpty(fullNameEditText.getText().toString()))
        {
            Toast.makeText(this, "Заполните имя.", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(userPassEditTxt.getText().toString()))
        {
            Toast.makeText(this, "Заполните пароль", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(addressEditText.getText().toString()))
        {
            Toast.makeText(this, "Заполните адрес доставки", Toast.LENGTH_SHORT).show();
        }
        else if(checker.equals("clicked"))
        {
            uploadImage();
        }
    }

    private void userInfoDisplay(final RecyclerView profileImageView,final EditText fullNameEditText,final EditText userPhoneEditText,final EditText addressEditText) {
        String phone = Prevalent.currentOnlineUser.getPhone();
        DatabaseReference UsersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(phone);

        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    if (dataSnapshot.child("image").exists())
                    {
                        String image = dataSnapshot.child("image").getValue().toString();
                        String name = dataSnapshot.child("name").getValue().toString();
                        String password = dataSnapshot.child("password").getValue().toString();
                        String address = dataSnapshot.child("address").getValue().toString();

                        String userImg = Paper.book().read(Prevalent.UserImageKey);
                        if (TextUtils.isEmpty(userImg))
                            Picasso.get().load(R.drawable.profile).placeholder(R.drawable.profile);
                        else
                            Picasso.get().load(userImg).placeholder(R.drawable.profile).into((Target) profileImageView);
                        fullNameEditText.setText(name);
                        userPassEditTxt.setText(password);
                        addressEditText.setText(address);
                    }

                    if (dataSnapshot.child("address").exists())
                    {
                        String name = dataSnapshot.child("name").getValue().toString();
                        String password = dataSnapshot.child("password").getValue().toString();
                        String address = dataSnapshot.child("address").getValue().toString();

                        fullNameEditText.setText(name);
                        userPassEditTxt.setText(password);
                        addressEditText.setText(address);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void uploadImage() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Обновляемся..");
        progressDialog.setMessage("Пожалуйста, подождите");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        if (imageUri != null)
        {
            final StorageReference fileRef = storageProfilePictureRef
                    .child(Prevalent.currentOnlineUser.getPhone() + ".WebP");

            uploadTask = fileRef.putFile(imageUri);

            uploadTask.continueWithTask(new Continuation() {
                        @Override
                        public Object then(@NonNull Task task) throws Exception
                        {
                            if (!task.isSuccessful())
                            {
                                throw task.getException();
                            }

                            return fileRef.getDownloadUrl();
                        }
                    })
                    .addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task)
                        {
                            if (task.isSuccessful())
                            {
                                Uri downloadUrl = task.getResult();
                                String myUrl = downloadUrl.toString();

                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");

                                HashMap<String, Object> userMap = new HashMap<>();
                                userMap. put("name", fullNameEditText.getText().toString());
                                userMap. put("address", addressEditText.getText().toString());
                                userMap. put("phoneOrder", userPhoneEditText.getText().toString());
                                userMap. put("image", myUrl);
                                ref.child(Prevalent.currentOnlineUser.getPhone()).updateChildren(userMap);

                                progressDialog.dismiss();

                                startActivity(new Intent(SettingsActivity.this, UsersHome.class));
                                Toast.makeText(SettingsActivity.this, "Информация успешно сохранена", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                            else
                            {
                                progressDialog.dismiss();
                                Toast.makeText(SettingsActivity.this, "Error.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        else
        {
            Toast.makeText(this, "Изображение не выбрано.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults, this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null){
            if (requestCode ==FilePickerConst.REQUEST_CODE_PHOTO)
            {
                arrayList = data.getParcelableArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA);

                profileImageView.setLayoutManager(new LinearLayoutManager(this));

                profileImageView.setAdapter(new MainAdp(arrayList));


            }
        }
    }

    private void imagePicker() {
        FilePickerBuilder.getInstance()
                .setActivityTitle("Select Images")
                .setSpan(FilePickerConst.SPAN_TYPE.FOLDER_SPAN,3)
                .setSpan(FilePickerConst.SPAN_TYPE.FOLDER_SPAN,4)
                .setMaxCount(1)
                .setSelectedFiles(arrayList)
                .setActivityTheme(R.style.CustomTheme)
                .pickPhoto(this);
    }


    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        if (requestCode ==100 && perms.size()==2){
            imagePicker();
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

        if (EasyPermissions.somePermissionPermanentlyDenied(this,perms)){
            new AppSettingsDialog.Builder(this).build().show();
        }else{
            Toast.makeText(getApplicationContext(),
                    "Permission Denied", Toast.LENGTH_SHORT).show();
        }

    }
}