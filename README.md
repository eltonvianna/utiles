# Summary

This is my implementation of some Java utilities.

## The back end development

To implement the back end I've decided to use only the Java 8 API, except the import of some test libraries, such JUnit, Mockito, etc, that are required to create and perform unit tests.
## Tdd aproach

The TDD approach was applied using JUnit as way to cover the unit tests.

## Prerequisites

Installation of **Java 8** and **Maven 3**. Add **JAVA_HOME/jre/bin** and **M2_HOME/bin** to the **PATH** environment variable. Also is required access to the internet as way to access the Maven Central repository.

## Getting Started

After cloned this project, change to **utiles** directory and run the command: **mvn clean install**. This command will perform the build, run the tests and packaging.

### Logging configuration

The current Java logging configuration is file based, editing the file application.properties you can change log level, add appenders, etc.

````
## The global logger (optional. Default: com.esv.utile.logging.core.logger.AsyncLogger)
#logging.logger=com.esv.utile.logging.core.logger.AsyncLogger

# The log file name (optional. Default: logging.log)
#logging.fileName=logging.log

# The rollover period. *IN SECONDS* (optional. Default: 3600s = 60 mins). The mininal rollver time is 5s.
#logging.rolloverPeriod=3600

# Enable compression after the log file rollover (optional. Default: true) 
#logging.rolloverGzipEnabled=true

# The timestamp sufix pattern after log rollover (optional. Default: yyyyMMddHHmmss)
#logging.timestampPattern=yyyyMMddHHmmss

# The global logging level (optional. Default: INFO).
logging.level=ALL

# The date pattern (optional. Default: yyyy-MM-dd'T'HH:mm:ss.SSSZ)
#logging.datePattern=yyyy-MM-dd'T'HH:mm:ss.SSSZ

# The log appenders (optional. Default: com.esv.utile.logging.core.appender.RollingLogAppender) See also: com.esv.utile.logging.core.appender.ConsoleLogAppender
#logging.appenders=com.esv.utile.logging.core.appender.RollingLogAppender

# The time waiting to consume the enqueued LogEvents. *IN MILLISECONDS* (optional. Default value: 100). The minimal time wait value is 100ms
#logging.async.logEventTimeWait=100

# The log layout (optional. Default: com.esv.utile.logging.core.StaticLogLayout)
#logging.logLayout=com.esv.utile.logging.core.StaticLogLayout
````

### Unexpected errors

You can open the log file to see more details about unexpected errors in the server side.

## Author

Elton S. Vianna <elton.vianna@yahoo.co.uk>.

## License

Copyright © 2017-2017 Elton Santos Vianna. Distributed under GNU General Public License v3.0.