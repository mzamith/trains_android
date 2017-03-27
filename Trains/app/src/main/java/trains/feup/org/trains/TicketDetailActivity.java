package trains.feup.org.trains;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class TicketDetailActivity extends AppCompatActivity {
    ImageView qrCodeImageview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_detail);

        qrCodeImageview=(ImageView) findViewById(R.id.img_qr_code_image);
    }
}
