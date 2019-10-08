##Overview
This is a simple scala cron parser used for parsing some cron parameters and output result in a specific format. 

[One of tons of doc about cron](https://docs.oracle.com/cd/E12058_01/doc/doc.1014/e12030/cron_expressions.htm)

###Requirements
- You'll need a `scala 2.13`
- You'll need `scalatest` and `scalactic` for running the tests.


###How to use/run
- This is sbt based project, you can import and build it in IntelliJ.
- `runner` is simple bash for running your build output from SBT.
- `runner` takes 6 parameters in this order: Minute, Hour, DayOfMonth, Month, DayOfWeek, Command.

    ```
    $ ./runner 2 3 */5 1-5 \* /lib/exec
    
    minute        2
    hour          3
    day of month  1 6 11 16 21 26
    month         1 2 3 4 5
    day of week   1 2 3 4 5 6 7
    command       /lib/exec
    ```
    
    Note: Make sure to skip the `*` in the terminal as shown above.

- If there is any parsing error, it will throw an `IllegalArgumentException`.
```
java.lang.IllegalArgumentException: requirement failed: Found: List(2, 3, */5, 1-5, *, /lib/exec, ds)
Please provide all 6 parameters: minute, hour, day of month, month, day of week, command
```
###Supported use cases
- `*/-,` and numbers are allowed in the parameters 


###Not Supported (yet)

- Comma separated patterns. e.g `1-5,6/10`
- Written Days patterns in the Day of the week e.g `SUN,MON`
- Written Months patterns e.g `JAN,FEB`
- Years parameters.
- Extra special characters like `LWC#?`




