# Prevoty-Deploy-Check
Checks the deployment of Prevoty by analyzing the Prevoty Diagnostic log file.

# Introduction
This document describes how to check the deployment of Prevoty by analyzing the Prevoty Diagnostic log file.

The analysis is based on a certain set of messages that need to appear in the Prevoty Diagnostic log.

These messages are based on:

1. The version of Prevoty being used. E.g.:
  1. 6.7
2. The filter insertion method being used:
  1. Automatic: agent-based by means of the JVM arguments or
  2. Manual: manual change of web.xml
3. The language being used:
  1. Java or
  2. .NET

In the case where the certain set of messages appear more than once in the Prevoty Diagnostic log file, the tool will analyze the latest set of messages.

# Preparations
1. Get a copy of the tool PrevotyCheckLog.jar from the Prevoty Customer Success Portal here
2. Copy PrevotyCheckLog.jar onto the server running Prevoty
3. Get a copy of the Prevoty JVM arguments from the same server
4. Get the value for -Dprevoty\_log\_config. E.g.:

/opt/Apache/Tomcat-8.5.5/Prevoty/prevoty\_logging.json

  1. This is the Prevoty Logging Configuration file
1. Open this Prevoty Logging Configuration file and get the value of default\_log\_directory. E.g.:

/opt/Apache/Tomcat-8.5.5/logs

  1. This is the directory where Prevoty will store the log files
1. From this Prevoty log directory note down the name of the latest Prevoty Diagnostic log file and prepend it with the value for the default\_log\_directory. E.g.:

/opt/Apache/Tomcat-8.5.5/logs/prevoty.log

  1. This is the absolute path of the Prevoty Diagnostic log file, which we will analyze

# Execution
1. Log on to the server running Prevoty
2. Open a terminal
3. Go into the directory where you have copied PrevotyCheckLog.jar. E.g.:

cd /tmp

1. Execute PrevotyCheckLog.jar with the following arguments
  1. The version of Prevoty being used. E.g.:

-v 3.6.7

  1. The type of Prevoty filter insertion (automatic or manual). E.g.:

-i manual

  1. The language you are using (java or .NET). E.g.:

-l java

  1. The Complete path of the Prevoty Diagnostic log file. E.g.:

-f /opt/Apache/Tomcat-8.5.5/logs/prevoty.log

  1. g.:
java -jar ./PrevotyCheckLog.jar -v 3.6.7 -i manual -l /java -f /opt/Apache/Tomcat-8.5.5/logs/prevoty.log
1. The tool will report two results
  1. A summary of the analysis. E.g.:

|Prevoty Diagnostic Log Summary|
|------------------------------|

Prevoty version: \&lt; 3.9.0

Prevoty filter insertion: Manual change web.xml

Prevoty language: Java

Prevoty diagnostic log file path: /Users/percy.rotteveel/Documents/Prevoty/Customers/Citi/Projects/170311-InView Client Web/issues/1127/Sep212018/prevoty.log

Prevoty deployment check: OK

  1. A detailed analysis. E.g.:

|Prevoty Diagnostic Log Analysis|
|-------------------------------|

String 0: Found

Search string: com.prevoty.agent.Agent - Prevoty Agent: Premain Invoked

Occurrence(s): 33460

Last occurrence: Sep 21 2018 21:31:35 UTC [main] INFO com.prevoty.agent.Agent - Prevoty Agent: Premain Invoked

String 1: Found

Search string: com.prevoty.agent.Agent - Prevoty Version:

Occurrence(s): 33461

Last occurrence: Sep 21 2018 21:31:35 UTC [main] INFO com.prevoty.agent.Agent - Prevoty Version: 3.6.7 (f7d255a3)

String 2: Found

Search string: com.prevoty.commons.configuration.ApplicationConfiguration - Found application configuration from disk:

Occurrence(s): 33470

Last occurrence: Sep 21 2018 21:31:35 UTC [main] INFO com.prevoty.commons.configuration.ApplicationConfiguration - Found application configuration from disk: /apps/ivclweb/appconfigs/codebase/properties/prevoty/CPB\_Prevoty.json

String 3: Found

Search string: com.prevoty.commons.configuration.ApplicationConfiguration - Successfully deserialized and loaded application configuration:

Occurrence(s): 33471

Last occurrence: Sep 21 2018 21:31:35 UTC [main] INFO com.prevoty.commons.configuration.ApplicationConfiguration - Successfully deserialized and loaded application configuration: /apps/ivclweb/appconfigs/codebase/properties/prevoty/CPB\_Prevoty.json

String 4: Found

Search string: com.prevoty.agent.AgentRuntime - Installing CmdinjectionAgentRuntime

Occurrence(s): 33478

Last occurrence: Sep 21 2018 21:31:35 UTC [main] INFO com.prevoty.agent.AgentRuntime - Installing CmdinjectionAgentRuntime

String 5: Found

Search string: com.prevoty.agent.AgentRuntime - Installing PathTraversalAgentRuntime

Occurrence(s): 33484

Last occurrence: Sep 21 2018 21:31:35 UTC [main] INFO com.prevoty.agent.AgentRuntime - Installing PathTraversalAgentRuntime

String 6: Found

Search string: com.prevoty.agent.AgentRuntime - Installing QueryAgentRuntime

Occurrence(s): 33494

Last occurrence: Sep 21 2018 21:31:35 UTC [main] INFO com.prevoty.agent.AgentRuntime - Installing QueryAgentRuntime

String 7: Found

Search string: com.prevoty.servlet.ServletContextListener - Prevoty Servlet Context Listener handling context event

Occurrence(s): 87465

Last occurrence: Sep 21 2018 21:33:02 UTC [server.startup : 0] INFO com.prevoty.servlet.ServletContextListener - Prevoty Servlet Context Listener handling context event.\

1. If there are one or more messages missing in the Prevoty Diagnostic log, that information can be used to further investigate what might be wrong with the Prevoty deployment. E.g.:

|Prevoty Diagnostic Log Summary|
|------------------------------|

Prevoty version: \&gt;= 3.9.0

Prevoty filter insertion: Agent-based

Prevoty language: Java

Prevoty diagnostic log file path: /Users/percy.rotteveel/Documents/Prevoty/Customers/Citi/Projects/161807-CVEP/Issues/1198/prevoty.log\_20180924

Prevoty deployment check: ERROR

|Prevoty Diagnostic Log Analysis|
|-------------------------------|

String 0: Found

Search string: com.prevoty.agent.Agent - Prevoty Agent: Premain Invoked

Occurrence(s): 25, 42, 421, 458

Last occurrence: Sep 24 2018 19:03:42 UTC [main] INFO com.prevoty.agent.Agent - Prevoty Agent: Premain Invoked

String 1: Found

Search string: com.prevoty.agent.Agent - Prevoty Version:

Occurrence(s): 26, 43, 422, 459

Last occurrence: Sep 24 2018 19:03:42 UTC [main] INFO com.prevoty.agent.Agent - Prevoty Version: 3.9.3 (b840d163)

String 2: Found

Search string: com.prevoty.commons.configuration.ApplicationConfiguration - Found application configuration from disk:

Occurrence(s): 32, 49, 428, 465

Last occurrence: Sep 24 2018 19:03:42 UTC [main] INFO com.prevoty.commons.configuration.ApplicationConfiguration - Found application configuration from disk: /opt/gportal/icg\_portal/dev/rev-proxy-citivelocity/etc/Prevoty/prevoty-CVEP\_RP\_monitor.json

String 3: Found

Search string: com.prevoty.commons.configuration.ApplicationConfiguration - Successfully deserialized and loaded application configuration:

Occurrence(s): 33, 50, 429, 466

Last occurrence: Sep 24 2018 19:03:42 UTC [main] INFO com.prevoty.commons.configuration.ApplicationConfiguration - Successfully deserialized and loaded application configuration: /opt/gportal/icg\_portal/dev/rev-proxy-citivelocity/etc/Prevoty/prevoty-CVEP\_RP\_monitor.json

String 4: Found

Search string: com.prevoty.agent.AgentRuntime - Installing CmdinjectionAgentRuntime

Occurrence(s): 38, 55, 434, 471

Last occurrence: Sep 24 2018 19:03:42 UTC [main] INFO com.prevoty.agent.AgentRuntime - Installing CmdinjectionAgentRuntime

String 5: Found

Search string: com.prevoty.agent.AgentRuntime - Installing PathTraversalAgentRuntime

Occurrence(s): 39, 56, 435, 472

Last occurrence: Sep 24 2018 19:03:42 UTC [main] INFO com.prevoty.agent.AgentRuntime - Installing PathTraversalAgentRuntime

String 6: Found

Search string: com.prevoty.agent.AgentRuntime - Installing QueryAgentRuntime

Occurrence(s): 40, 57, 436, 473

Last occurrence: Sep 24 2018 19:03:42 UTC [main] INFO com.prevoty.agent.AgentRuntime - Installing QueryAgentRuntime

String 7: Found

Search string: com.prevoty.agent.AgentRuntime - Installing ServletAPIAgentRuntime

Occurrence(s): 41, 58, 437, 474

Last occurrence: Sep 24 2018 19:03:42 UTC [main] INFO com.prevoty.agent.AgentRuntime - Installing ServletAPIAgentRuntime

String 8: Not found

Search string: com.prevoty.agent.Agent - Using RASP jar:

Occurrence(s): None

String 9: Not found

Search string: com.prevoty.agent.Agent - Agent version:

Occurrence(s): None

  1. In this case we are using Prevoty \&gt;= 3.9.0 with agent-based filter insertion where &quot;Using RASP jar&quot; and &quot;Agent version&quot; were not reported, it might mean the two additional JVM arguments were not set. E.g.:

-Dprevoty\_agentrasp

-Dprevoty\_raspjarpath=/opt/gportal/icg\_portal/dev/rev-proxy-citivelocity/etc/Prevoty/prevoty-rasp-3.9.3.jar&quot;

RackMultipart20210423-4-1g9xa53.docx September 30, 2018 Page 8 of 8
