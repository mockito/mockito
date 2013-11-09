![Mockito](http://docs.mockito.googlecode.com/hg/latest/org/mockito/logo.jpg)
  
simplier & better mocking

[![Build Status](https://travis-ci.org/mockito/mockito.png?branch=master)](https://travis-ci.org/mockito/mockito) [![Coverage Status](https://coveralls.io/repos/mockito/mockito/badge.png)](https://coveralls.io/r/mockito/mockito)

## Current release
06/10/2012 Mockito **1.9.5** released! See the release notes. Should appear in maven central shortly.

## Moving to github
We are currently moving a few stuff from [Google Code](https://code.google.com/p/mockito/) to [Github](https://github.com/mockito/mockito).

For now only the code repository is moved, issue tracker might follow, but other stuff might stay there like the wiki pages and documentation, discussion group will stay at google.

## Why drink it?
Mockito is a mocking framework that tastes really good. It lets you write beautiful tests with clean & simple API. Mockito doesn't give you hangover because the tests are very readable and they produce clean verification errors. Read more about [features & motivations](https://code.google.com/p/mockito/wiki/FeaturesAndMotivations).

> *"We decided during the main conference that we should use JUnit 4 and Mockito because we think they are the future of TDD and mocking in Java"* - Dan North, the originator of BDD

[More quotes](https://code.google.com/p/mockito/wiki/Quotes)

> Over 15000 downloads of 1.9.0 version ('12), excluding maven/Gradle users. For latest figures see the downloads.

[More about the user base](https://code.google.com/p/mockito/wiki/UserBase)

## How do I drink it?

Download [mockito-all-x.x.x.jar](http://code.google.com/p/mockito/downloads/list) and put it on the classpath. If you use a fancy build system with declarative dependencies like Gradle or Maven please -> [Click HERE](https://code.google.com/p/mockito/wiki/DeclaringMockitoDependency) <-

### Then you can verify interactions

```java
import static org.mockito.Mockito.*;

// mock creation
List mockedList = mock(List.class);

// using mock object ; observe that it didn't throw any "unexpected interaction exception" exception
mockedList.add("one");
mockedList.clear();

// selective & explicit verification
verify(mockedList).add("one");
verify(mockedList).clear();
```

### Or stub method calls

```java
// you can mock concrete classes, not only interfaces
LinkedList mockedList = mock(LinkedList.class);

// stubbing; before the actual execution
when(mockedList.get(0)).thenReturn("first");

// the following prints "first"
System.out.println(mockedList.get(0));

// the following prints "null" because get(999) was not subbed
System.out.println(mockedList.get(999));
```

### You can go further

* Try the annotations [@Mock](http://docs.mockito.googlecode.com/hg/latest/org/mockito/Mock.html), [@Spy](http://docs.mockito.googlecode.com/hg/latest/org/mockito/Spy.html), [@Captor](http://docs.mockito.googlecode.com/hg/latest/org/mockito/Captor.html), [@InjectMocks](http://docs.mockito.googlecode.com/hg/latest/org/mockito/InjectMocks.html)
* Try BDD syntax with [BDDMockito](http://docs.mockito.googlecode.com/hg/latest/org/mockito/BDDMockito.html)
* If the provided answers doesn't fit your needs, write one yourself extending the [Answer](http://docs.mockito.googlecode.com/hg/latest/org/mockito/stubbing/Answer.html) interface
* Try the Mockito on Android, thanks to the Google guys working on dexmaker (more on that later)

### Remember

* Do not mock types you don't own
* Don't mock value objects
* Don't mock everything
* Show some love with your tests

Click here for more documentation and examples. Remember all documentation lives in javadocs so you don’t need to visit that page too often. You can grab the RefCard? here.

If you have any suggestions, find documentation unclear or you found a bug, write to our [mailing list](http://groups.google.com/group/mockito). You can report bugs [here](http://code.google.com/p/mockito/issues/list).

## Who is your bartender?
Mockito is served to you by **Szczepan Faber and friends**. First people who tried Mockito were developers of the [Guardian](http://guardian.co.uk/) project in London in early 2008. Here is how Szczepan explained [why we need another mocking framework](http://monkeyisland.pl/2008/01/14/mockito)?

Firstly, hats down before **[EasyMock](http://easymock.org/) folks** for their ideas on beautiful and refactorable mocking syntax. First hacks on Mockito were done on top of the EasyMock? code.

Here are just some of my friends who contributed ideas to Mockito (apologize if I missed somebody): **Igor Czechowski**, **Patric Fornasier**, **Jim Barritt**, **Felix Leipold**, **Liz Keogh**, **Bartosz Bańkowski**.

Now some other people joined the gang : **Brice Dutheil** and **David Wallace**.

Special thanks to **Erik Ramfelt**, and **Steve Christou** for putting Mockito on his Jenkins server (continuous integration).

Thanks to **Karol Poźniak** for the logo :)

Finally, thanks to **Erik Brakkee** who helps us getting jars to maven central

## links wrap-up

### Wiki
* [FAQ](https://code.google.com/p/mockito/wiki/FAQ)
* [HowToContribute](https://code.google.com/p/mockito/wiki/HowToContribute)
* [MockitoForPython](https://code.google.com/p/mockito/wiki/MockitoForPython)
* [MockitoVSEasyMock](https://code.google.com/p/mockito/wiki/MockitoVSEasyMock)
* [RelatedProjects](https://code.google.com/p/mockito/wiki/RelatedProjects)
* [ReleaseNotes](https://code.google.com/p/mockito/wiki/ReleaseNotes)
* [More wiki pages [on google code]](https://code.google.com/p/mockito/w/list)

### Blogs
* [Szczepan on Mockito](http://monkeyisland.pl/category/mockito)
* [Brice's French coffee workshop](http://blog.arkey.fr/)

### Other project links
* [Latest documentation](http://docs.mockito.googlecode.com/hg/latest/org/mockito/Mockito.html)
* [Dzone Reference Card](http://refcardz.dzone.com/refcardz/mockito)
* [Hudson continuous integration server](http://hudsonci-oss.org/view/Mockito/job/Mockito)

### Groups
* [Mockito mailing list](http://groups.google.com/group/mockito)
