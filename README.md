How to compile and run application on a Windows or Linux Machine from the command line using downloadable trace files:<br />
* [MINIFE.t](https://drive.google.com/file/d/1VT88k8sWPrV9LTUu_ndKNhsMzejNpD-Z/view?usp=sharing)<br />
* [XSBENCH.t](https://drive.google.com/file/d/1VT88k8sWPrV9LTUu_ndKNhsMzejNpD-Z/view?usp=sharing)<br />


1. Navigate inside the source-code directory. Make sure the trace files are placed in the same directory.  

2. To compile use the command:<br />
`javac *java`

3. To run use the command:<br />
`java -Xmx2048m Main <CACHE_SIZE> <ASSOCIATIVITY> <REPLACEMENT_POLICY> <WRITE_BACK_POLICY> <TRACE_FILE>`

`Replace <CACHE_SIZE> with a value between 8KB and 128KB, make sure value is a power of 2.`<br />
`Replace <ASSOCIATIVITY> with a value between 1 and 64, make sure value is a power of 2.`<br />
`Replace <REPLACEMENT_POLICY> with a 0 to represent LRU, or a 1 to represent FIFO.`<br />
`Replace <WRITE_BACK_POLICY> with a 0 to represent write-through, or a 1 to represent write-back.`<br />
`Replace <TRACE_FILE> with MINIFE.t or XSBENCH.t.`<br />

Example:<br /> 
`javac *java`<br />
`java -Xmx2048m Main 32768 8 1 1 XSBENCH.t`