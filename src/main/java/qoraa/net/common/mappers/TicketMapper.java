package qoraa.net.common.mappers;

import qoraa.net.common.ticket.TicketConverter;

public class TicketMapper {

    public Long fromTicket(String ticketId) {
	return TicketConverter.fromDtoToInternal(ticketId, Long.class);
    }

    public String toTicket(Long id) {
	return TicketConverter.fromInternalToDto(id);
    }
}
