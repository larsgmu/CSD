/*General  monitor  (java.util.concurrent)  with  a  single  condition  variable  for the whole territory.*/

import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Terrain1 implements Terrain {
    Viewer v;
    ReentrantLock lock;
    Condition occupied;

    public  Terrain1 (int t, int ants, int movs) {
        v = new Viewer(t,ants,movs,"1");

        lock = new ReentrantLock();
        occupied = lock.newCondition();

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
                    occupied.await();
                } catch (InterruptedException e) {
                }
                v.retry(a);
            }
            v.go(a);
            occupied.signalAll();
        } finally {
            lock.unlock();
        }
    }
}