public class Pool1 extends Pool {
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

    public synchronized void kidRests() {
        kids_swimming--;
        log.resting();
        notifyAll();
        //notify();
    }

    public synchronized void instructorSwims() {
        instructors_swimming++;
        log.swimming();
        notifyAll();
        //notify();
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
