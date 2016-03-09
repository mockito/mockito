- [Contributing to Mockito](#)
	- [Pull request criteria](#)
	- [General info](#)
	- [More on pull requests](#)

**If looking for support**

* Search / Ask question on [stackoverflow](http://stackoverflow.com)
* Go to the [mockito mailing-list](http://groups.google.com/group/mockito) (moderated)
* Issues should always have a [Short, Self Contained, Correct (Compilable), Example](http://sscce.org) (same as any question on stackoverflow.com)

# Contributing to Mockito

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
* Wondering what to work on? See [task/bug list](https://github.com/mockito/mockito/issues/) [(old task/bug list)](http://code.google.com/p/mockito/issues/list) and pick up something you would like to work on. Remember that some feature requests we somewhat not agree with so not everything we want to work on :)
* Mockito currently uses GitHub for versioning so you can *create a fork of Mockito in no time*. Go to the [github project](https://github.com/mockito/mockito) and "Create your own fork". Create a new branch, commit, ..., when you're ready let us know about your pull request so we can discuss it and merge the PR!
* Note the project now uses *gradle*, when your Gradle install is ready, make your IDE project's files (for example **`gradle idea`**). Other gradle commands are listed via **`gradle tasks`**.

## More on pull requests

As you may have noticed, Mockito has now a continuous release bot, that means that each **merged** pull request will be automatically released in a newer version of Mockito.
For that reason each pull request has to go through a thorough review and/or discussion.

Things we pay attention in a PR :

* On pull requests, please document the change, what it brings, what is the benefit. If the issue exists on the old issue tracker on [Google Code](https://code.google.com/p/mockito/issues/list), please add cross links. But don't create a new one there.
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


_If you are unsure about git you can have a look on our [git tips to have a clean history](https://github.com/mockito/mockito/wiki/Using git to prepare your PR to have a clean history)._

