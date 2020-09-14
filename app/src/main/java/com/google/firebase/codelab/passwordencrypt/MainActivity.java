package com.google.firebase.codelab.passwordencrypt;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Message;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.SecretKeySpec;

public class MainActivity extends AppCompatActivity {
    EditText inputText,inputPassword;
    TextView outputText;
    Button encBtn,decBtn;
    String outputsString;
    String AES="AES";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputText=findViewById(R.id.inputText);
        inputPassword=findViewById(R.id.password);

        outputText=findViewById(R.id.outputText);
        encBtn=findViewById(R.id.encbtn);
        decBtn=findViewById(R.id.decbtn);

        encBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    outputsString=encrypt(inputText.getText().toString() ,inputPassword.getText().toString());
                    outputText.setText(outputsString);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        decBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    outputsString=decrpt(outputsString,inputPassword.getText().toString());
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this,"WrongPassword",Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                outputText.setText(outputsString);
            }
        });
    }

    private String decrpt(String outputsString, String password) throws Exception {
        SecretKeySpec key = generateKey(password);
        Cipher c = Cipher.getInstance(AES);
        c.init(Cipher.DECRYPT_MODE,key);
        byte[] decoderValue = Base64.decode(outputsString,Base64.DEFAULT);
        byte[] decValue = c.doFinal(decoderValue);
        String decryptedValue = new String(decValue);
        return decryptedValue;

    }

    private String encrypt(String Data, String password) throws Exception {
        SecretKeySpec key = generateKey(password);
        Cipher c = Cipher.getInstance(AES);
        c.init(Cipher.ENCRYPT_MODE,key);
        byte[] encVal = c.doFinal(Data.getBytes());
        return Base64.encodeToString(encVal,Base64.DEFAULT).toString();


    }

    private SecretKeySpec generateKey(String password) throws Exception {
        final MessageDigest digest=MessageDigest.getInstance("SHA-256");
        byte[] bytes = password.getBytes("UTF-8");
        digest.update(bytes, 0,bytes.length);
        byte[] key = digest.digest();
        SecretKeySpec secretKeySpec=new SecretKeySpec(key,"AES");
        return secretKeySpec;

    }


}