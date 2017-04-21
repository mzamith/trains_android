package trains.feup.org.tickets.util;

import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import trains.feup.org.tickets.model.TicketInspectorDTO;

/**
 * Created by Renato on 4/19/2017.
 */

public class Decrypter {

    private static final String TAG = Decrypter.class.getName();

    private static SecretKeySpec secretKey;

    private static final String SECRET_KEY = "L£(iIab£qh#TXCLE§€DpOD]=b$WgZeliLirYDXQs£NoFdOB?nlFyRkyr€mV\"UtdffV%PoKDP#VitGTQYS=%Hu/xDHoYXS§e{iK!xmsAZjQFM=\"nx#UQA(EMWPt€€d%§w";

    private static void setKey() {
        if (secretKey != null) {
            return;
        }
        MessageDigest sha;
        byte[] key;
        try {
            key = SECRET_KEY.getBytes("UTF-8");
            sha = MessageDigest.getInstance("SHA-256");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            secretKey = new SecretKeySpec(key, "AES");
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException ex) {
            Logger.getLogger(Decrypter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static TicketInspectorDTO decrypt(String text) throws JsonSyntaxException {

        setKey();
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            text = new String(cipher.doFinal(Base64.decode(text, Base64.DEFAULT)));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }

        Log.i(TAG, "Content: " + text);

        Gson gson = new Gson();
        return gson.fromJson(text, TicketInspectorDTO.class);
    }
}
