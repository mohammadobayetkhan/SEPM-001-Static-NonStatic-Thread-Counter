Here we can see every time either in the unsafe state or safe state the non static total is always same as the expected count . 
We can say that for some example such as having only one thread and increments per thread is 1000 so the expected count is the
multiplication of the number of thread and the number of increment per thread ,so the expected count stands is 1000 and after run 
the class we get the non static count is same as the expected count.Then we can see the other example like N(Thread),K(increments per thread)
is 2,10000 then expected count and the total non static count is 20000 same as for higher number of threads . But for the static count we can see some
differences (from the result anylysis ,given in the pdf file) such as when the number of thread is 1 then the expected count and the static count or unsafe count is same
but when we increasing the number of thread then the unsafe count is not same as the expected count and also we can see in the output section that
every time we execute the program the number of unsafe count is changing thus the absolute difference is also changing and there is no pattern
becuse of the race condition.
