package codeEditor.buffer;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class SynchronizedBuffer implements Buffer {   
    BlockingQueue buffer = new LinkedBlockingQueue() {};
    
    @Override
    public void put(Object request) throws InterruptedException {
        buffer.put(request);
    }
    
    @Override
    public Object take() throws InterruptedException{
        return buffer.take();
    }
    
    @Override
    public boolean isEmpty() {
        return buffer.isEmpty();
    }
    
}