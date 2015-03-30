package snorreedwin.no.myofirebase.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;

import com.facebook.crypto.Crypto;
import com.facebook.crypto.Entity;
import com.facebook.crypto.exception.CryptoInitializationException;
import com.facebook.crypto.exception.KeyChainException;
import com.facebook.crypto.keychain.SharedPrefsBackedKeyChain;
import com.facebook.crypto.util.SystemNativeCryptoLibrary;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class Obscurifier {
    Crypto crypto;
    SharedPreferences prefs;
    Entity tokenEntity;

    public Obscurifier(Context context, SharedPreferences preferences) {
        prefs = preferences;
        crypto = new Crypto(
                new SharedPrefsBackedKeyChain(context),
                new SystemNativeCryptoLibrary()
        );
        tokenEntity = new Entity("token");
    }

    public void cryptAndSaveString(String string) {
        if(!crypto.isAvailable()) {
            return;
        }
        String save = "";
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            OutputStream outputStream = crypto.getCipherOutputStream(out, tokenEntity);
            outputStream.write(string.getBytes());
            outputStream.close();
            save = Base64.encodeToString(out.toByteArray(), Base64.DEFAULT);
        } catch (IOException e) {
            Log.e("CryptoObscurifier", "IO Exception, " + e);
        } catch (CryptoInitializationException e) {
            Log.e("CryptoObscurifier", "Crypto Initialization exception, "+e);
        } catch (KeyChainException e) {
            Log.e("CryptoObscurifier", "Key chain exception, "+e);
        }

        prefs.edit().putString("token",save).apply();
    }

    public String getUncryptFirebaseAdminToken() {
        String encryptedToken = prefs.getString("token", null);
        try {
            InputStream inputStream = crypto.getCipherInputStream(
                    new ByteArrayInputStream(Base64.decode(encryptedToken.getBytes(), Base64.DEFAULT)),
                    tokenEntity);

            int read;
            byte[] buffer = new byte[1024];
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            while ((read = inputStream.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            inputStream.close();
            out.close();
            return new String(out.toByteArray());
        } catch (Exception e) {
            Log.e("CryptoObscurifier", "Exception, "+e);
        }
        return null;
    }
}
