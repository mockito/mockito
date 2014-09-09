package org.mockito.release.notes.internal

import org.mockito.release.notes.Improvement

import static org.mockito.release.notes.internal.ChangeSetSegregator.NON_EXISTING_LABEL

class ImprovementsPrinter {

    private static final String DEFAULT_HEADER_FOR_OTHER_IMPROVEMENTS = "Other"

    private final ChangeSetSegregator segregator
    private final Map<String, String> labelToListHeaderMappings

    ImprovementsPrinter(ChangeSetSegregator segregator, Map<String, String> labelToListHeaderMappings, String headerForOther) {
        this.segregator = segregator
        this.labelToListHeaderMappings = labelToListHeaderMappings +
                [(NON_EXISTING_LABEL): headerForOther ?: DEFAULT_HEADER_FOR_OTHER_IMPROVEMENTS]
    }

    String print(Collection<Improvement> improvements) {
        def tokenizedChanges = segregator.tokenize(improvements)
        def printedTokenizedChanges = printTokenizedImprovements(tokenizedChanges)

        "* Changes: ${improvements.size()}\n * $printedTokenizedChanges"
     }

    private String printTokenizedImprovements(Map<String, List<Improvement>> tokenizedImprovementsMap) {
        tokenizedImprovementsMap.collect { entry ->
            "${labelToListHeaderMappings.getOrDefault(entry.key, entry.key)}: ${printOneSubImprovements(entry.value)}"
        }.join('\n * ')
    }

    private String printOneSubImprovements(Collection<Improvement> improvements) {
        if (improvements.isEmpty()) {
            "0"
        } else {
            "${improvements.size()}\n  * ${improvements.join('\n  * ')}"
        }
    }
}
