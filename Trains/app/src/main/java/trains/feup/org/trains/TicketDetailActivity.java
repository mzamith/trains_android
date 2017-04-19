package trains.feup.org.trains;

import android.database.Cursor;
import android.database.sqlite.SQLiteAbortException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import trains.feup.org.trains.storage.TicketsContract;
import trains.feup.org.trains.storage.TicketsDbHelper;

import static trains.feup.org.trains.TrainsApp.getContext;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

public class TicketDetailActivity extends AppCompatActivity {


    private String id;
    private long date;
    private String departure;
    private long departureTime;
    private String destination;
    private double price;
    private TextView dateView;
    private TextView departureView;
    private TextView destinationView;
    private TextView priceView;

    ImageView qrCodeImageview;
    String qrContent;
    String errorMsg = "";
    public final static int WIDTH=200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_detail);

        TicketsDbHelper ticketsDbHelper = new TicketsDbHelper(getContext());
        try (SQLiteDatabase db = ticketsDbHelper.getReadableDatabase()) {
            Cursor cursor;

            String[] projection = {
                    TicketsContract.TicketEntry.COLUMN_ID,
                    TicketsContract.TicketEntry.COLUMN_DATE,
                    TicketsContract.TicketEntry.COLUMN_DEPARTURE,
                    TicketsContract.TicketEntry.COLUMN_DEPARTURE_TIME,
                    TicketsContract.TicketEntry.COLUMN_DESTINATION,
                    TicketsContract.TicketEntry.COLUMN_PRICE,
                    TicketsContract.TicketEntry.COLUMN_STATE,
                    TicketsContract.TicketEntry.COLUMN_CODE_DTO
            };

            String selection = TicketsContract.TicketEntry.COLUMN_ID + " = ?";

            long TicketID = getIntent().getExtras().getLong("Ticket_ID");
            id = String.valueOf(TicketID);

            String[] selectionArgs = {id};

            String sortOrder = TicketsContract.TicketEntry.COLUMN_ID + " DESC";

            cursor = db.query(
                    TicketsContract.TicketEntry.TABLE_NAME,                     // The table to query
                    projection,                               // The columns to return
                    selection,                                // The columns for the WHERE clause
                    selectionArgs,                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    sortOrder                                 // The sort order
            );

            cursor.moveToNext();
            date = cursor.getLong(cursor.getColumnIndexOrThrow(TicketsContract.TicketEntry.COLUMN_DATE));
            departure = cursor.getString(cursor.getColumnIndexOrThrow(TicketsContract.TicketEntry.COLUMN_DEPARTURE));
            departureTime = cursor.getLong(cursor.getColumnIndexOrThrow(TicketsContract.TicketEntry.COLUMN_DEPARTURE_TIME));
            destination = cursor.getString(cursor.getColumnIndexOrThrow(TicketsContract.TicketEntry.COLUMN_DESTINATION));
            price = cursor.getLong(cursor.getColumnIndexOrThrow(TicketsContract.TicketEntry.COLUMN_PRICE));
            price *= 0.1;
            qrContent = cursor.getString(cursor.getColumnIndexOrThrow(TicketsContract.TicketEntry.COLUMN_CODE_DTO));
            cursor.close();

            Log.i("SQLITE","WENT THROUGH THE CURSOR");

        } catch (SQLiteAbortException e){
            Log.e("SQLLite:", e.getStackTrace().toString());
        }

        Log.i("SQLITE READING", "GOT READABLE DATABASE: departure = " + departure + "; destination = " + destination + "; date = " + date + "; price = " + price + ";" );

        dateView = (TextView) findViewById(R.id.ticket_departure_date);
        departureView = (TextView) findViewById(R.id.ticket_departure);
        destinationView = (TextView) findViewById(R.id.ticket_destination);
        priceView = (TextView) findViewById(R.id.ticket_price);

        //Value attribution to each View
        DateFormat df = new SimpleDateFormat("dd/MM/yy");
        String departureDateString = df.format(new Date(date));

        DateFormat tf = new SimpleDateFormat(" 'at' HH:mm");
        String departureTimeString = tf.format(new Date(departureTime));

        String departureDateTime = departureDateString + departureTimeString;

        dateView.setText(departureDateTime);
        departureView.setText(departure);
        destinationView.setText(destination);

        //TODO remove hardcoded currency values
        priceView.setText(String.format("%.2f €", price));


        qrCodeImageview=(ImageView) findViewById(R.id.img_qr_code_image);

        Thread t = new Thread(new Runnable() {  // do the creation in a new thread to avoid ANR Exception
            public void run() {
                final Bitmap bitmap;
                try {
                    bitmap = encodeAsBitmap(qrContent);
                    runOnUiThread(new Runnable() {  // runOnUiThread method used to do UI task in main thread.
                        @Override
                        public void run() {
                            qrCodeImageview.setImageBitmap(bitmap);
                        }
                    });
                }
                catch (WriterException e) {
                    errorMsg += "\n" + e.getMessage();
                }
                if (!errorMsg.isEmpty())
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                        }
                    });
            }
        });
        t.start();
    }

    Bitmap encodeAsBitmap(String str) throws WriterException {
        BitMatrix result;
        try {
            result = new MultiFormatWriter().encode(str, BarcodeFormat.QR_CODE, WIDTH, WIDTH, null);
        }
        catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }
        int w = result.getWidth();
        int h = result.getHeight();
        int[] pixels = new int[w * h];
        for (int y = 0; y < h; y++) {
            int offset = y * w;
            for (int x = 0; x < w; x++) {
                pixels[offset + x] = result.get(x, y) ? getResources().getColor(R.color.colorPrimary):getResources().getColor(R.color.white);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, w, 0, 0, w, h);
        return bitmap;
    }
}
