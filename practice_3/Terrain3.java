import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Terrain3 implements Terrain {
    Viewer v;
    ReentrantLock lock;
    Condition[] occupied;

    private int grid_size;

    public  Terrain3 (int t, int ants, int movs) {
        v = new Viewer(t,ants,movs,"2");
        lock = new ReentrantLock();
        grid_size = t;

        occupied = new Condition[grid_size*grid_size];

        for (int i=0; i < grid_size; i++)
            for(int j=0; j < grid_size; j++){
                occupied[j + i*grid_size] = lock.newCondition();
            }

        for (int i=0; i<ants; i++){
            new Ant(i,this,movs).start();
        }

    }

    public void hi(int a) {
        lock.lock();
        try {
            v.hi(a);
        } finally {
            lock.unlock();
        }
    }

    public void bye(int a) {
        lock.lock();
        try {
            v.bye(a);
        } finally {
            lock.unlock();
        }
    }

    public void move(int a) throws InterruptedException {
        lock.lock();
        try {
            v.turn(a);
            Pos dest=v.dest(a);
            while (v.occupied(dest)) {
                try {
                    occupied[dest.x*grid_size + dest.y].await(300, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                }
                //v.retry(a);
                v.chgDir(a);
                dest = v.dest(a);
            }
            Pos current_position = v.getPos(a);
            v.go(a);
            occupied[current_position.x*grid_size + current_position.y].signalAll();
        } finally {
            lock.unlock();
        }
    }
}