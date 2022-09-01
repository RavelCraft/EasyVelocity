package com.github.imdabigboss.easyvelocity.utils;

import com.github.imdabigboss.easyvelocity.EasyVelocity;

public class Lock {
    private boolean isLocked = false;

    public synchronized void lock() {
        try {
            while (isLocked) {
                wait();
            }
            isLocked = true;
        } catch (InterruptedException e) {
            EasyVelocity.getLogger().error("Lock exception", e);
        }
    }

    public synchronized void unlock() {
        isLocked = false;
        notify();
    }
}
