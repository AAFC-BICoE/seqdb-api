#!/usr/bin/awk -f
{
    line = $0;
    while (match(line,"\$[A-Za-z0-9\._-!]+")) {  # string beginning with $
#           RSTART is where the pattern starts
#           RLENGTH is the length of the pattern
            before = substr(line,1,RSTART-1);
            variable = substr(line,RSTART+1,RLENGTH-1);
            value = ENVIRON[variable];
            if (value == "")
                 value = "[UNKNOWN]";
            after = substr(line,RSTART+RLENGTH);
            line = (before value after);
    }
    printf("%s\n", line);
}
