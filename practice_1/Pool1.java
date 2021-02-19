// CSD feb 2015 Juansa Sendra


public class Pool1 extends Pool {   //no kids alone
    public static int instructors_swimming = 0;
    public static int kids_swimming = 0;

    public void init(int ki, int cap) {
    }

    public synchronized void kidSwims() throws InterruptedException {
        while (instructors_swimming == 0){
            log.waitingToSwim();
            wait();
        }
        kids_swimming++;
        log.swimming();
    }

    public synchronized void kidRests() throws InterruptedException {
        kids_swimming--;
        log.resting();

        //notify instructors wanting to rest;
        notifyAll();
    }

    public synchronized void instructorSwims() throws InterruptedException {
        instructors_swimming++;
        log.swimming();

        //notify kids wanting to swim
        //notify instructors wanting to rest
        notifyAll();
    }

    public synchronized void instructorRests() throws InterruptedException {
        while (instructors_swimming == 1 && kids_swimming >= 1) {
            log.waitingToRest();
            wait();
        }
        instructors_swimming--;
        log.resting();
    }
}
