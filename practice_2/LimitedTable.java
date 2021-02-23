// CSD Mar 2013 Juansa Sendra

public class LimitedTable extends RegularTable { //max 4 in dinning-room
    public static int seats_occupied = 0;
    public LimitedTable(StateManager state) {super(state);}

    public synchronized void enter(int id) throws InterruptedException {
        while (seats_occupied >= 4){
            state.wenter(id);
            wait();
        }
        seats_occupied++;
        state.enter(id);
    }
    public synchronized void exit(int id) {
        seats_occupied--;
        state.exit(id);
        notifyAll();
    }
}
