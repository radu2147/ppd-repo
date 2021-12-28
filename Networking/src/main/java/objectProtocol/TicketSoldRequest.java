package objectProtocol;

import domain.FestivalDTO;
import domain.TicketDTO;

public class TicketSoldRequest implements UpdateResponse{
    private TicketDTO ticketDTO;

    public TicketSoldRequest(TicketDTO ticketDTO) {
        this.ticketDTO = ticketDTO;
    }

    public TicketDTO getTicketDTO() {
        return ticketDTO;
    }

    public void setTicketDTO(TicketDTO ticketDTO) {
        this.ticketDTO = ticketDTO;
    }
}
