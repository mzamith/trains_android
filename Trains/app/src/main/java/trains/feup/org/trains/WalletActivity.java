package trains.feup.org.trains;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;

import trains.feup.org.trains.api.ServerListCallback;
import trains.feup.org.trains.model.Ticket;
import trains.feup.org.trains.model.Travel;
import trains.feup.org.trains.service.TicketService;
import trains.feup.org.trains.service.TripService;
import trains.feup.org.trains.util.JsonUtil;

public class WalletActivity extends DrawerActivity {

    ListView ticketList;
    ArrayList<Ticket> tickets = new ArrayList<>();

    ArrayAdapter<Ticket> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_wallet, null, false);
        drawer.addView(contentView, 0);

        ticketList = (ListView) findViewById(R.id.tickets_list);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        ticketList.setAdapter(adapter);

        TicketService service = TicketService.getInstance();

        service.getTickets(this, new ServerListCallback() {

            @Override
            public void OnSuccess(JSONArray result) {

                Gson gson = new Gson();
                Ticket[] ticketsArray = gson.fromJson(result.toString(), Ticket[].class);
                tickets = new ArrayList<>(Arrays.asList(ticketsArray));

                adapter.addAll(tickets);

            }

            @Override
            public void OnError(int errorCode) {

            }
        });

    }
}
