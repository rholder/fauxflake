[![Build Status](http://img.shields.io/travis/rholder/fauxflake.svg)](https://travis-ci.org/rholder/fauxflake) [![Coverage Status](http://img.shields.io/coveralls/rholder/fauxflake.svg)](https://coveralls.io/r/rholder/fauxflake?branch=master) [![Latest Version](http://img.shields.io/badge/latest-1.1.0-brightgreen.svg)](https://github.com/rholder/fauxflake/releases/tag/v1.1.0) [![License](http://img.shields.io/badge/license-apache%202-brightgreen.svg)](https://github.com/rholder/fauxflake/blob/master/LICENSE)

##What is this?
Fauxflake is an easily embeddable, decentralized, k-ordered unique ID generator.
It can use the same encoded ID format as Twitter's Snowflake or Boundary's
Flake implementations as well as any other customized encoding without too much
effort. The `fauxflake-core` module has no external dependencies and is meant to
be about as light as possible while still delivering useful functionality.
Essentially, if you want to be able to generate a unique identifier across your
infrastructure with reasonable assurances about collisions, then you might find
this useful.

##The Problem
You've run into one or more of the following scenarios:
* You need to generate a unique identifier, perhaps for an RDBMS primary key or
for a NoSQL storage abstraction, and you need it done fast.
* You need to be able to guarantee the loose orderings of unique keys when more
than one machine is generating them.
* A centralized key generation service seems too heavy or is otherwise unacceptable.
* You've thought about clock drift, server time running backwards, or
any of a number of horrible things you read [here](http://infiniteundo.com/post/25509354022/more-falsehoods-programmers-believe-about-time-wisdom)
that make relying on simple system timestamps for anything absolutely
horrifying.

##Where can I get it?
You can find this module in the Maven Central repository by using your favorite
build tool:
###Maven

    <dependency>
      <groupId>com.github.rholder.fauxflake</groupId>
      <artifactId>fauxflake-core</artifactId>
      <version>1.1.0</version>
    </dependency>

###Gradle

    compile "com.github.rholder.fauxflake:fauxflake-core:1.1.0"

##Quickstart
Let's say you want to generate a lexicographically sortable, Twitter compatible
identifier, as a String. You could do that with the following snippet:
```java
    IdGenerator snowflake = IdGenerators.newSnowflakeIdGenerator();
    String id = snowflake.generateId(1000).asString();
```

The Snowflake and Flake `IdGenerator` instances are both thread-safe and
intended to be used as singletons.

##Customization
The `MachineIdProvider` is meant to be customized when the "good enough"
guarantees coming from the `MacMachineIdProvider` and the
`MacPidMachineIdProvider`aren't really good enough for your use case and you
need more strictness in how those are being set. For instance, by just swapping
in your own unique machine identifier from a system property, you can get an
actual universal guarantee among all of your machines if each are given a unique
number on startup:

```java
public class CustomMachineIdProvider implements MachineIdProvider {
    @Override
    public long getMachineId() {
        return Long.valueOf(System.getProperty("my.app.machine.id"));
    }
}
```

You might want to note that a Snowflake ID only has 1024 possible unique machine
identifiers since the specification calls for reserving 10 bits for this
information.

##Additional Documentation
Javadoc can be found [here](http://rholder.github.io/fauxflake/javadoc/1.1.0/fauxflake-core/index.html).

##Building from source
The Fauxflake project uses a [Gradle](http://gradle.org)-based build system. In the instructions
below, [`./gradlew`](http://vimeo.com/34436402) is invoked from the root of the source tree and serves as
a cross-platform, self-contained bootstrap mechanism for the build. The only
prerequisites are [Git](https://help.github.com/articles/set-up-git) and JDK 1.6+.

### check out sources
`git clone git://github.com/rholder/fauxflake.git`

### compile and test, build all jars
`./gradlew build`

### install all jars into your local Maven cache
`./gradlew install`

##License
The Fauxflake module is released under version 2.0 of the
[Apache License](http://www.apache.org/licenses/LICENSE-2.0).

##References
* [https://github.com/twitter/snowflake/](https://github.com/twitter/snowflake/)
* [https://blog.twitter.com/2010/announcing-snowflake](https://blog.twitter.com/2010/announcing-snowflake)
* [https://github.com/boundary/flake](https://github.com/boundary/flake)
* [http://boundary.com/blog/2012/01/12/flake-a-decentralized-k-ordered-unique-id-generator-in-erlang/](http://boundary.com/blog/2012/01/12/flake-a-decentralized-k-ordered-unique-id-generator-in-erlang/)
* [http://infiniteundo.com/post/25509354022/more-falsehoods-programmers-believe-about-time-wisdom](http://infiniteundo.com/post/25509354022/more-falsehoods-programmers-believe-about-time-wisdom)
* [http://engineering.custommade.com/simpleflake-distributed-id-generation-for-the-lazy/](http://engineering.custommade.com/simpleflake-distributed-id-generation-for-the-lazy/)
