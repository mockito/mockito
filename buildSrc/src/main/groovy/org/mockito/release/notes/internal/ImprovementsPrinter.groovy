package org.mockito.release.notes.internal

import static ImprovementSetSegregator.NON_EXISTING_LABEL

class ImprovementsPrinter {

    private static final String ROOT_IMPROVEMENTS_HEADER = "Improvements"
    private static final String DEFAULT_HEADER_FOR_OTHER_IMPROVEMENTS = "Other"

    private final ImprovementSetSegregator segregator
    private final Map<String, String> labelToListHeaderMappings

    ImprovementsPrinter(ImprovementSetSegregator segregator, Map<String, String> labelToListHeaderMappings, String headerForOther) {
        this.segregator = segregator
        this.labelToListHeaderMappings = labelToListHeaderMappings +
                [(NON_EXISTING_LABEL): headerForOther ?: DEFAULT_HEADER_FOR_OTHER_IMPROVEMENTS]
    }

    String print(Collection<Improvement> improvements) {
        def tokenizedImprovements = segregator.tokenize(improvements)
        def printedTokenizedImprovements = printTokenizedImprovements(tokenizedImprovements)

        "* " + ROOT_IMPROVEMENTS_HEADER + ": ${improvements.size()}$printedTokenizedImprovements"
     }

    private String printTokenizedImprovements(Map<String, List<Improvement>> tokenizedImprovementsMap) {
        tokenizedImprovementsMap.findAll { entry ->
            entry.value.size() > 0
        }.collect { entry ->
                "\n  * ${labelToListHeaderMappings.get(entry.key, entry.key)}: ${printOneSubImprovements(entry.value)}"
        }.join('')
    }

    private String printOneSubImprovements(Collection<Improvement> improvements) {
        if (improvements.isEmpty()) {
            "0"
        } else {
            "${improvements.size()}\n    * ${improvements.join('\n    * ')}"
        }
    }
}
