var usingOldIE = $.browser.msie && parseInt($.browser.version) < 9;
if(!usingOldIE) {
    $("head").append("<link rel=\"shortcut icon\" href=\"{@docRoot}/favicon.ico?v=cafebabe\">");
    $("head", window.parent.document).append("<link rel=\"shortcut icon\" href=\"{@docRoot}/favicon.ico?v=cafebabe\">");
    hljs.initHighlightingOnLoad();
    $("pre.code").css("font-size", "0.9em");
    injectProjectVersionForJavadocJDK6("Mockito ${project.version} API",
        "em#mockito-version-header-javadoc7-header",
        "em#mockito-version-header-javadoc7-footer");
}
