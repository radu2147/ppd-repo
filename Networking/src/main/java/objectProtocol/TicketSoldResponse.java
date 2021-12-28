package objectProtocol;

import domain.TicketDTO;

public class TicketSoldResponse implements UpdateResponse{
    private TicketDTO ticketDTO;

    public TicketSoldResponse(TicketDTO ticketDTO) {
        this.ticketDTO = ticketDTO;
    }

    public TicketDTO getTicketDTO() {
        return ticketDTO;
    }

    public void setTicketDTO(TicketDTO ticketDTO) {
        this.ticketDTO = ticketDTO;
    }
}
