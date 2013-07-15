[![Build Status](https://travis-ci.org/rholder/fauxflake.png)](https://travis-ci.org/rholder/fauxflake)

##What is this?
TODO write this

##Maven

    <dependency>
      <groupId>com.github.rholder</groupId>
      <artifactId>fauxflake-core</artifactId>
      <version>1.0.0</version>
    </dependency>

##Gradle

    compile "com.github.rholder:fauxflake-core:1.0.0"

##Quickstart
TODO write this

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