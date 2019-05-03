- [Contributing to Mockito](#contributing-to-mockito)
    - [Pull request criteria](#pull-request-criteria)
    - [General info](#general-info)
    - [More on pull requests](#more-on-pull-requests)
    - [Coding style](#coding-style)

**If looking for support**

* Search / Ask question on [stackoverflow](http://stackoverflow.com/questions/tagged/mockito)
* Go to the [mockito mailing-list](http://groups.google.com/group/mockito) (moderated)
* Issues should always have a [Short, Self Contained, Correct (Compilable), Example](http://sscce.org) (same as any question on stackoverflow.com)

# Contributing to Mockito

Which branch : 
* On mockito 2.x, make your pull request target `release/2.x`
* On next mockito version make your pull request target `master`

## Pull request criteria

* **At least one commit message** in the PR starts with `Fixes #id : ` where `id` is an [issue tracker](https://github.com/mockito/mockito/issues) id. This allows automated release notes generation. Also GitHub will track the issue and [close it](https://github.com/blog/1386-closing-issues-via-commit-messages) when the PR is merged.
* Use `@since` tags for new public APIs
* Include tests
* Document public APIs with examples
* For new features consider adding new documentation item in main `Mockito` class
* Also, look at the [GitHub's Pull Request guide](https://github.com/blog/1943-how-to-write-the-perfect-pull-request)

## General info

* Comment on issues or pull requests
* If you know the answer to a question posted to our [mailing list](https://groups.google.com/forum/#!forum/mockito) - don't hesitate to write a reply. That helps us a lot.
* Also, don't hesitate to ask questions on the [mailing list](https://groups.google.com/forum/#!forum/mockito) - that helps us improve javadocs/FAQ.
* Please suggest changes to javadoc/exception messages when you find something unclear.
* If you miss a particular feature in Mockito - browse or ask on the mailing list, show us a sample code and describe the problem.
* Wondering what to work on? See [task/bug list](https://github.com/mockito/mockito/issues/) and pick up something you would like to work on. Remember that some feature requests we somewhat not agree with so not everything we want to work on :)
* Mockito currently uses GitHub for versioning so you can *create a fork of Mockito in no time*. Go to the [github project](https://github.com/mockito/mockito) and "Create your own fork". Create a new branch, commit, ..., when you're ready let us know about your pull request so we can discuss it and merge the PR!
* Note the project now uses *gradle*, when your Gradle install is ready, make your IDE project's files (for example **`gradle idea`**). Other gradle commands are listed via **`gradle tasks`**.

## More on pull requests

As you may have noticed, Mockito has now a continuous release bot, that means that each **merged** pull request will be automatically released in a newer version of Mockito.
For that reason each pull request has to go through a thorough review and/or discussion.

Things we pay attention in a PR :

* On pull requests, please document the change, what it brings, what is the benefit.
* **Clean commit history** in the topic branch in your fork of the repository, even during review. That means that commits are _rebased_ and _squashed_ if necessary, so that each commit clearly changes one things and there are no extraneous fix-ups.

  For that matter it's possible to commit [_semantic_ changes](http://lemike-de.tumblr.com/post/79041908218/semantic-commits). _Tests are an asset, so is history_.

  _Exemple gratia_:

  ```
  Fixes #73 : The new feature
  Fixes #73 : Refactors this part of Mockito to make feature possible
  ```

* In the code, always test your feature / change, in unit tests and in our `acceptance test suite` located in `org.mockitousage`. Older tests will be migrated when a test is modified.
* New test methods should follow a snake case convention (`ensure_that_stuff_is_doing_that`), this allows the test name to be fully expressive on intent while still readable.
* When reporting errors to the users, if it's a user report report it gently and explain how a user should deal with it, see the `Reporter` class. However not all errors should go there, some unlikely technical errors don't need to be in the `Reporter` class.
* Documentation !!! Always document with love the public API. Internals could use some love too. In all cases the code should _auto-document_ itself like any [well designed API](rebased and squashed if necessary, so that each commit clearly changes one things and there are no extraneous fix-ups).
* We use (4) spaces instead of tabs. Make sure line ending is Unix style (LF). More on line ending on the [Github help](https://help.github.com/articles/dealing-with-line-endings/).


_If you are unsure about git you can have a look on our [git tips to have a clean history](https://github.com/mockito/mockito/wiki/Using-git-to-prepare-your-PR-to-have-a-clean-history)._


## Coding style

This section is not about some kind of fruitless tabs vs spaces debate. It's about having the code readable, the project grows and it is not rare to read contributions from many different individuals. Each one of us has a different writing style, we are fine with this. Without enforcing we may be picky about it, however we think that this improves the readability of the code for everyone.

_This includes IntelliJ IDEA instructions, however we are sure there's similar settings in all major IDEs._

But first of all, make sure that : 

* Don't use tabs, only spaces
* Character encoding is **UTF-8**
* Line ending character is unix-style **`LF`**
* New line is added at end of file: `IntelliJ setting > Editor > General > Ensure line feed at file end on save`

For most editors, this should be automatically enforced by [EditorConfig](http://editorconfig.org/).
Check if your editor has a built-in plugin or if you need to download one.
IntelliJ has a built-in plugin, for Eclipse you need to download [this plugin](https://github.com/ncjones/editorconfig-eclipse#readme).

If you want to check if your code complies to the style guide, you can run:

* `./gradlew checkstyleMain` to check the main source code
* `./gradlew checkstyleTest` to check the test source code
* `./gradlew check` to check main and test source code, and run the tests

### Imports

Imports must be sorted in the following order

1. `import java.*`
2. `import javax.*`
3. `import all other imports`
4. blank line
5. `import static java.*`
6. `import static javax.*`
7. `import static all other imports`

This order can be set in `IntelliJ setting > Editor > Code Style > Java > Imports > Import Layout`

Also make sure that
* One blank lines before imports.
* One blank lines after imports.
* Never import with wildcard `*`
   * Set `IntelliJ setting > Editor > Code Style > Java > Imports > Class count to use import with '*'` to `100`     
   * Set `IntelliJ setting > Editor > Code Style > Java > Imports > Names count to use import static with '*'` to `100`     

### Alignment

We found vertical alignment helping when reading the code, for that reason we want to align vertically chained APIs, parameters, etc. For that reason the spacing characters are spaces.


1. For chained calls, when multiple lines make more sense, we want method calls to be aligned
    vertically with previous dot.

    ```java
    mock(Foo.class, withSettings().name("bar")
                                  .serializableMode(ACROSS_CLASSLOADER)
                                  .verboseLogging().invocationListener(...));
    ```

    Go to `IntelliJ setting > Editor > Code Style > Java > Wrapping and Braces`

    1. For parameter `Chained method calls` choose : `Do not wrap`
    2. For sub-parameter `Align when multiline` tick the checkbox

2. When declaring a function with several parameters and when multiple lines make sense,
    we want to align vertically the method parameters and arguments

    ```java
    void feature(String key,
                 RepresentsSomething something) {
        // ...
    }
    ```

    Go to `IntelliJ setting > Editor > Code Style > Java > Wrapping and Braces`

    1. For parameter `Method declaration parameters` choose : `Do not wrap`
    2. For sub-parameter `Align when multiline` tick the checkbox

3. When using a function with several parameters and when multiple lines make sense,
    we want to align vertically the method parameters and arguments

    ```java
    given(mock.action()).willReturn("a very long param",
                                    "b another very long parameter",
                                    "c yet another");
    ```

    Go to `IntelliJ setting > Editor > Code Style > Java > Wrapping and Braces`

    1. For parameter `Method call parameters` choose : `Do not wrap`
    2. For sub-parameter `Align when multiline` tick the checkbox

4. When declaring an annotation with several parameters and when multiple lines make sense,
    we want to align vertically the annotation parameters

    ```java
    @Mock(answer = Answers.RETURNS_DEFAULTS,
          serializable = true, 
          extraInterfaces = { List.class, YetAnotherInterface.class })
    ```

    Go to `IntelliJ setting > Editor > Code Style > Java > Wrapping and Braces`

    1. For parameter `Annotation Parameters` choose : `Do not wrap`
    2. For sub-parameter `Align when multiline` tick the checkbox

4. When declaring a throws list with several exception and when multiple line make sense,
    we want to align vertically the exceptions parameters

    ```java
    void feature() throws IOException,
                          YetAnotherException {
         // ...
    }
    ```

    Go to `IntelliJ setting > Editor > Code Style > Java > Wrapping and Braces`

    1. For parameter `Throws list` choose : `Do not wrap`
    2. For sub-parameter `Align when multiline` tick the checkbox

