package trains.feup.org.tickets;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Renato on 4/11/2017.
 */

public class DepartureHolder extends RecyclerView.ViewHolder {

    private final TextView departureName;
    private final TextView departureHour;
    private final TextView departureFrom;
    private final Button downloadTicketsButton;

    public DepartureHolder(View itemView) {
        super(itemView);

        departureName = (TextView) itemView.findViewById(R.id.departure_name);
        departureFrom = (TextView) itemView.findViewById(R.id.departure_from);
        departureHour = (TextView) itemView.findViewById(R.id.departure_hour);
        downloadTicketsButton = (Button) itemView.findViewById(R.id.download_tickets_button);

        downloadTicketsButton.setText("Download");

    }

    public TextView getDepartureName() {
        return departureName;
    }

    public TextView getDepartureHour() {
        return departureHour;
    }

    public TextView getDepartureFrom() {
        return departureFrom;
    }

    public Button getDownloadTicketsButton() {
        return downloadTicketsButton;
    }
}
