package org.mockito.release.notes

import org.mockito.release.notes.util.HumanReadable
import spock.lang.Specification
import spock.lang.Subject

class NotesPrinterTest extends Specification {

    @Subject printer = new NotesPrinter()

    def "prints notes"() {
        def date = new Date(1483570800000)
        when: def notes = printer.printNotes("2.0.1", date, {"the contributions"} as HumanReadable, {"the improvements"} as HumanReadable)
        then: notes == """### 2.0.1 (2017-01-04 23:00 UTC)

the contributions
the improvements

"""
    }
}
