function injectProjectVersionForJavadocJDK6(projectVersionString, javadoc7Header, javadoc7Footer) {
    // register custom function to remove attributes from an element
    jQuery.fn.removeAttributes = function() {
        return this.each(function() {
            var attributes = $.map(this.attributes, function(item) {
                return item.name;
            });
            var img = $(this);
            $.each(attributes, function(i, item) {
                img.removeAttr(item);
            });
        });
    };

    $(function () {
        /* Add name & version to header for Javadoc 1.6 */
        $("td.NavBarCell1[colspan=2]").each(function(index, element) {
            var jqueryTD = $(element);
            jqueryTD.after(
                $("<td><em><strong>" + projectVersionString + "</strong></em></td>").attr("class","NavBarCell1").attr("id","project-version-header-javadoc6")
            );
            jqueryTD.removeAttr("colspan");
        });
        /* Cleans up mess with Javadoc 1.7 */
        $("body > h1").removeAttributes().attr("class", "bar").attr("title", projectVersionString);
        /* Cleans up mess with Javadoc 1.7 with Javadoc 1.6 */
        $("td " + javadoc7Header).remove();
        $("td " + javadoc7Footer).remove();
    });
}
