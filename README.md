# tema2

Tema 2 APD 

    In aceasta tema am implementat ın Java un sistem de planificare a taskurilor intr-un 
datacenter.
    Am pornit de la un schelet al temei si am dezvoltat algoritmii pentru datacenter si host. 

    Am avut de implementat patru politici de planificare, anume Round Robin - RR, Shortest 
Queue - SQ, Size Interval Task Assignment - SITA si Least Work Left - LWL.
    Ca si constrangeri a trebuit sa tinem cont de:
    	- prioritatea taskurilor
    	- posibilitatea ca un task sa poata fi preemptat
    	- timpul de start al taskului si durata acestuia
    	- numarul de hosts pasat in fiecare task
    	
    In dispatcher am implementat politicile de planificare.
    Pentru Round Robin am determinat hostul care trebuie sa primeasca taskul (hostToUse, 
primul utilizat fiind hostul 0), folosind algoritmul indicat in tema si i-am trimis taskul 
hostului (addTask). Hostul l-am salvat in variabila globala taskUsedHost pentru urmatoarea 
iteratie.
    Pentru Shortest Queue am interogat (getQueueSize - metoda scrisa in host) fiecare host 
pentru a determina lungimea cozilor de asteptare, adaugand si taskul aflat in executie daca 
exista. S-a pasat taskul hostului cu coada de asteptare de lungime minima.
    Pentru Size Interval Task Assignment taskul a fost atribuit celor trei hosturi pe baza 
lungimii taskului primita ca parametru.
    Pentru Least Work Left atribuirea taskului s-a facut pe baza duratei totale a calculelor 
ramase de executat la nivelul nodurilor. In acest scop s-a definit o variabila globala la 
nivel de host, getWorkLeft, crescuta la adaugarea unui task in coada de asteptare si 
decrementata dupa fiecare secunda petrecuta de un task in run.

    Pentru a realiza hostul s-au implementat urmatoarele:
    - in metoda addTask se adauga noul task primit in coada de asteptare fara a o sorta 
sau aranja in vreun fel. Singurele actiuni sunt marirea duratei de procesare a cozii (workLeft) 
si initializarea la nivel de task a duratei ramase (task.setleft) cu durata de procesare 
a taskului.
    - s-a creat o metoda getNextRunningTaskID care returneaza ID-ul taskului care va rula 
in continuare. Poate fi sau taskul aflat in executie sau un task nou (daca taskul aflat in 
executie s-a terminat sau daca este preemptibil si gasesc in coada un task cu prioritate mai 
mare, care respecta conditiile de a fi ales : prioritate maxima, apoi ID minim). 
    - s-a creat o metoda getNextRunningTask care ia din coada si returneaza taskul care 
va fi executat. Daca taskul aflat in executie a fost intrerupt este adaugat in coada de asteptare.
    - alte doua metode, getQueueSize si getWorkLeft au fost deja mentionate. Ultima procedura, 
shutdown, reseteaza o variabila definita la nivel de host, runningHost, care tine in bucla 
infinita rularea hostului.
    - metoda run (principala) gestioneaza functionarea hostului. Functioneaza in bucla controlata 
de o variabila care poate fi resetata prin procedura shotdown. In ordine 
    	+ apeleaza getNextRunningTaskID pentru a determina ID-ul taskului ce urmeaza sa ruleze
    	+ daca s-a schimbat taskul in executie apeleaza getNextRunningTask pentru a obtine taskul ce 
urmeaza sa ruleze si eventual adauga in coada de asteptare taskul aflat in executie daca a fost 
intrerupt
    	+ pune in asteptare threadul curent (hostul) o secunda, timp in care taskul aflat in 
executie poate rula
    	+ seteaza (decrementeaza) timpul ramas pentru taskul aflat in executie si, daca acesta a 
fost finalizat (task.left = 0) apeleaza procedura task.finish pentru a marca momentul finalizarii. 
    Pentru a memora timpul scurs in secunde se foloseste variabila currentTime.



    
