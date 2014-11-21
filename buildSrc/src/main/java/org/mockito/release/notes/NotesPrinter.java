package org.mockito.release.notes;

import org.mockito.release.notes.util.HumanReadable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

class NotesPrinter {

    public String printNotes(String version, Date date, HumanReadable contributions, HumanReadable improvements) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm z");
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        String now = format.format(date);

        return "### " + version + " (" + now + ")" + "\n\n"
                + contributions.toText() + "\n"
                + improvements.toText() + "\n\n";
    }
}
