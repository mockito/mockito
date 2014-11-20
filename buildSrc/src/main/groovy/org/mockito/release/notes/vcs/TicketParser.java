package org.mockito.release.notes.vcs;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class TicketParser {

    /**
     * Collects all ticked ids found in message, ticket format is #123
     */
    static Set<String> parseTickets(String message) {
        Set<String> tickets = new LinkedHashSet<String>();
        Pattern ticket = Pattern.compile("#\\d+");
        Matcher m = ticket.matcher(message);
        while(m.find()) {
            String ticketId = m.group().substring(1); //remove leading '#'
            tickets.add(ticketId);
        }
        return tickets;
    }
}
