GCWeb
=====
Copyright 2013 Krisa Chaijaroen under LGPL License (Please see included LICENSE)

Web Interface for GCViewer built on standard Java EE 5. Work best on Google Chrome Webkit Engine & IE10.
<img src="http://krisachai.files.wordpress.com/2013/10/gcweb.png?w=590">
Feature
=====
* Support both local file systems and remote GC log file via SSH (Tested on RHEL 5)
* Search for latest GC log file by default if destination is a directory (Both local and remote). Filename must contains "gc".
* Automatically update GC data (Configurable)
* Easy (but not safe) configuration via YAML, no database required.
* Blazing fast & Interactive web interface thanks to Dygraphs
* Show GC timeline in Date & Time unit.
* Export GC graph as PNG thanks to dygraphs-export
* Simple HTTP API

Limitation
=====
* Support only Young, Tenured, Heap utilization and GC Time. (Can't show full gc, yet)

How to Build
=====
* Build latest GCViewer [https://github.com/chewiebug/GCViewer]
* Create maven local repository for GCViewer Jar file [http://maven.apache.org/guides/mini/guide-3rd-party-jars-local.html]
* include GCViewer in pom.xml for example
    ```<dependency>
    <groupId>com.tagtraum</groupId>
    <artifactId>gcviewer</artifactId>
    <version>1.33</version>
    </dependency>
```
* mvn clean install

How to Use
=====
* edit included sources.yml and copy it to folder $user.home/conf/gcweb/ (eg. /usr/home/jbossusr/conf/gcweb, c:\user\owner\conf)
* [option] copy or edit log4j_conf.xml and put it into the same folder as sources.yml
* deploy GCWeb.war to Java EE 5 application server (Tested on Weblogic 10.3.5 and Jboss AS 7.1 with JDK 7)

Performance
=====
I have tuned this thing to an acceptable level with limited number of GC logs. UX should always fast as every gc log data is cached.
Mainly, performance depends on how big your GC log is and how number of cpu core your server has. 
If refresh rate is fast, you might need to adjust Quartz thread pool to have a higher number of thread. 
Tips, don't trigger ssh type gc log, too much, at the same time to prevent ssh timeout (3 seconds to locate target gc log).

=====
* Faster string processing, from parsed GC events to CSV. Now it took around 2-3 seconds to convert ~8,000 events to CSV.
* Full GC support.

License
=====
GCWeb uses
* GCViewer Copyright (c) 2002-2008 tagtraum industries incorporated.Copyright (c) 2011-2013 Joerg Wuethrich under [LGPL license] [https://github.com/chewiebug/GCViewer/blob/master/LICENSE.txt]
* DyGraph Copyright (c) 2006-, Dan Vanderkam. [MIT license][https://github.com/danvk/dygraphs/blob/master/LICENSE.txt]
* dygraphs-export [MIT license][http://cavorite.com/labs/js/dygraphs-export/]
* Bootstrap Copyright 2013 Twitter, Inc under [the Apache 2.0 license](LICENSE) [https://raw.github.com/twbs/bootstrap/master/README.md]
* jQuery Copyright 2013 jQuery Foundation and other contributors [MIT License] [https://github.com/chewiebug/GCViewer/blob/master/LICENSE.txt]
* ColorBox Copyright 2013 Jack Moore. [MIT license][https://github.com/jackmoore/colorbox/blob/master/README.md]
* Apache Commons Copyright © 2013 The Apache Software Foundation [the Apache 2.0 license][http://commons.apache.org/]
* Apache Mina SSHD Copyright © 2013 The Apache Software Foundation [the Apache 2.0 license][http://mina.apache.org/mina-project/]
* Apache Log4j Copyright © 2013 The Apache Software Foundation [the Apache 2.0 license][http://logging.apache.org/log4j/1.2/]
* Jettison Copyright 2006 Envoi Solutions LLC [the Apache 2.0 license][http://jettison.codehaus.org/License]
* jsch Copyright (c) 2002-2012 Atsuhiko Yamanaka, JCraft,Inc. [BSD license][http://www.jcraft.com/jsch/LICENSE.txt]
* quartz Copyright (c) Terracotta, Inc. [the Apache 2.0 license][http://quartz-scheduler.org/overview/license-and-copyright]
* slf4j Copyright (c) 2004-2013 QOS.ch [MIT license][http://www.slf4j.org/license.html]
* snakeyaml [the Apache 2.0 license][https://code.google.com/p/snakeyaml/]
