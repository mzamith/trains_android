package trains.feup.org.trains;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import trains.feup.org.trains.api.ServerListCallback;
import trains.feup.org.trains.api.ServerObjectCallback;
import trains.feup.org.trains.model.Ticket;
import trains.feup.org.trains.model.Travel;
import trains.feup.org.trains.service.TicketService;
import trains.feup.org.trains.service.TripService;
import trains.feup.org.trains.util.JsonUtil;

import static trains.feup.org.trains.TrainsApp.getContext;

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

        adapter = new TicketsAdapter(new ArrayList<Ticket>());
        ticketList.setAdapter(adapter);

        ticketList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?>  adapter, View v, int position, long id){

                //TODO CONNECT WITH TICKET DETAIL!!

                Ticket item = (Ticket) adapter.getItemAtPosition(position);

                Intent intent = new Intent(WalletActivity.this, TicketDetailActivity.class);

                //useful? idk
                intent.putExtra(getString(R.string.extra_ticket), item);
                startActivity(intent);

              //  Toast.makeText(WalletActivity.this, "Clicked on item" + item.getCode(), Toast.LENGTH_LONG).show();
            }
        });



        checkConnectionErrors();

        TicketService service = TicketService.getInstance();

        service.getTickets(this, new ServerListCallback() {

            @Override
            public void OnSuccess(JSONArray result) {

                adapter.clear();

                Gson gson = new Gson();
                Ticket[] ticketsArray = gson.fromJson(result.toString(), Ticket[].class);
                tickets = new ArrayList<>(Arrays.asList(ticketsArray));

                adapter.notifyDataSetChanged();
                adapter.addAll(tickets);
            }

            @Override
            public void OnError(int errorCode) {

            }
        });

    }

    private void checkConnectionErrors(){

        int errorCode = getIntent().getIntExtra(getString(R.string.error_connection), 0);

        switch (errorCode){
            case ServerObjectCallback.NOT_FOUND:
                Toast.makeText(this, "Make sure you have Wifi Connection", Toast.LENGTH_LONG).show();
                break;
        }
    }


    public class TicketsAdapter extends ArrayAdapter<Ticket> {

        public TicketsAdapter(List<Ticket> items) {
            super(WalletActivity.this, R.layout.ticket_row, items);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View v = convertView;

            if (v == null) {
                LayoutInflater vi;
                vi = LayoutInflater.from(getContext());
                v = vi.inflate(R.layout.ticket_row, null);
            }

            final Ticket ticket = getItem(position);

            if (ticket != null) {
                View colorBar = v.findViewById(R.id.color_ticket);
                TextView departure = (TextView) v.findViewById(R.id.ticket_departure);
                TextView arrival = (TextView) v.findViewById(R.id.ticket_arrival);
                TextView date = (TextView) v.findViewById(R.id.ticket_day);

                departure.setText(ticket.getFrom().getLabel());
                arrival.setText(ticket.getTo().getLabel());

                DateFormat df = new SimpleDateFormat("dd/MM");
                date.setText(df.format(new Date(ticket.getDay())));

                switch (ticket.getState()){
                    case "RESERVED":
                        colorBar.setBackgroundColor(getColor(R.color.amber_700));
                        break;
                    case "DENIED":
                        colorBar.setBackgroundColor(getColor(R.color.red_700));
                        break;
                    case "VALIDATED":
                        colorBar.setBackgroundColor(getColor(R.color.green_700));
                        break;
                }

            }

            return v;
        }

    }
}
