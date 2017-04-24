package codeEditor.buffer;

public interface Buffer {
    public void put(Object element) throws InterruptedException;
    public Object take() throws InterruptedException;
    public boolean isEmpty();
}