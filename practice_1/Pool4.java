public class Pool4 extends Pool {

    public static int instructors_swimming = 0;
    public static int kids_swimming = 0;
    public int max_kids_per_instructor = -1;
    public int max_swimmers = -1;
    public static int instructors_waiting_to_rest = 0;

    public void init(int ki, int cap) {
        max_kids_per_instructor = log.nk / log.ni;
        max_swimmers = (log.nk + log.ni) / 2;
    }

    public synchronized void kidSwims() throws InterruptedException {
        while (instructors_swimming == 0
                || ((kids_swimming + 1) > (max_kids_per_instructor * instructors_swimming))
                || ((kids_swimming+instructors_swimming + 1) > max_swimmers)
                || (instructors_waiting_to_rest > 0)){
            log.waitingToSwim();
            wait();
        }
        System.out.format("Instructors waiting to rest: %d \n", instructors_waiting_to_rest);
        kids_swimming++;
        log.swimming();
    }

    public synchronized void kidRests() throws InterruptedException {
        kids_swimming--;
        log.resting();
        notifyAll();
    }

    public synchronized void instructorSwims() throws InterruptedException {
        while ((kids_swimming+instructors_swimming+1) > max_swimmers) {
            log.waitingToSwim();
            wait();
        }
        instructors_swimming++;
        log.swimming();
        notifyAll();
    }

    public synchronized void instructorRests() throws InterruptedException {
        while ((instructors_swimming == 1 && kids_swimming >= 1)
                || (kids_swimming > (max_kids_per_instructor * (instructors_swimming-1)))) {
            instructors_waiting_to_rest++;
            log.waitingToRest();
            wait();
        }
        log.resting();
        instructors_swimming--;
        if (instructors_waiting_to_rest > 0) {
            instructors_waiting_to_rest--;
        }
    }
}