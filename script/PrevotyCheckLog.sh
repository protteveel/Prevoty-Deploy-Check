#!/bin/bash
clear
cd /Users/percy.rotteveel/Documents/workspace/PrevotyCheckLog
DIR="/Users/percy.rotteveel/Documents/Prevoty/Customers/Overstock.com/project/PartnerConnect/issue/1814/prevoty_blocked_logs_ppa100"
java -jar ./PrevotyCheckLog.jar -l java -i Automatic -v 3.10.3 -f "$DIR/prevoty.log"
