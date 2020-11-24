How to compile and run application on a Windows or Linux Machine from the command line using trace files:<br />

1. Navigate inside the source-code directory. Make sure the trace files are placed in the same directory.  

2. To compile use the command:<br />
`javac Main.java`

3. To run use the command:<br />
`java Main <M> <N> <TRACE_FILE>`

`Replace <M> with the number of PC bits used to index the gshare table.`<br />
`Replace <N> with the number of global history register bits.`<br />
`Replace <TRACE_FILE> with gobmk_trace.txt or mcf_trace.txt.`<br />

Example:<br /> 
`javac Main.java`<br />
`java Main 10 4 gobmk_trace.txt`
