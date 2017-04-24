package codeEditor.transform.operationalTransform;

import codeEditor.operation.OperationType;
import codeEditor.operation.Operation;
import codeEditor.operation.userOperations.EraseOperation;
import codeEditor.operation.userOperations.InsertOperation;
import codeEditor.operation.userOperations.RepositionOperation;

public class OperationalTransform {
    public static Operation transform(Operation o1, Operation o2, boolean flag) {
        if (o1.type == OperationType.INSERT) {
            InsertOperation op1 = (InsertOperation) o1;
            if (o2.type == OperationType.INSERT) {
                InsertOperation op2 = (InsertOperation) o2;
                return applyTransform(op1, op2, flag);
            } else if (o2.type == OperationType.ERASE) {
                EraseOperation op2 = (EraseOperation) o2;    
                return applyTransform(op1, op2, flag);
            } else if (o2.type == OperationType.REPOSITION) {
                RepositionOperation op2 = (RepositionOperation) o2;
                return applyTransform(op1, op2, flag);    
            }
        } else if (o1.type == OperationType.ERASE) {
            EraseOperation op1 = (EraseOperation) o1;    
            if (o2.type == OperationType.INSERT) {
                InsertOperation op2 = (InsertOperation) o2;
                return applyTransform(op1, op2, flag);
            } else if (o2.type == OperationType.ERASE) {
                EraseOperation op2 = (EraseOperation) o2;    
                return applyTransform(op1, op2, flag);
            } else if (o2.type == OperationType.REPOSITION) {
                RepositionOperation op2 = (RepositionOperation) o2;
                return applyTransform(op1, op2, flag);    
            }
        }
        return o2;
    }
    
    public static Operation applyTransform(InsertOperation o1, InsertOperation o2, boolean flag) {
        InsertOperation transformed = new InsertOperation(o2);
        if (flag) {
            if (o1.position <= o2.position) {
                transformed.position++;
            }
        } else {
            if (o1.position < o2.position) {
                transformed.position++;
            }
        }
        return transformed;
    }
    
    public static Operation  applyTransform(InsertOperation o1, EraseOperation o2, boolean flag) {
        EraseOperation transformed = new EraseOperation(o2);
        if (flag) {
            if (o1.position < o2.position) {
                transformed.position++;
            }
        } else {
            if (o1.position <= o2.position) {
                transformed.position++;
            }
        }
        return transformed;
    }
    
    public static Operation  applyTransform(EraseOperation o1, InsertOperation o2, boolean flag) {
        InsertOperation transformed = new InsertOperation(o2);
        if (flag) {
            if (o1.position < o2.position) {
                transformed.position--;
            }
        } else {
            if (o1.position <= o2.position) {
                transformed.position--;
            }
        }
        return transformed;
    }
    
    public static Operation  applyTransform(EraseOperation o1, EraseOperation o2, boolean flag) {
        EraseOperation transformed = new EraseOperation(o2);
        if (flag) {
            if (o1.position < o2.position) {
                transformed.position--;
            }
        } else {
            if (o1.position <= o2.position) {
                transformed.position--;
            }
        }
        return transformed;
    }
    
    public static Operation applyTransform(InsertOperation o1, RepositionOperation o2, boolean flag) {
        RepositionOperation transformed = new RepositionOperation(o2);
        if (flag) {
            if (o1.position < o2.position) {
                transformed.position++;
            }
        } else {
            if (o1.position <= o2.position) {
                transformed.position++;
            }
        }
        return transformed;
    }
    
    public static Operation applyTransform(EraseOperation o1, RepositionOperation o2, boolean flag) {
        RepositionOperation transformed = new RepositionOperation(o2);
        if (flag) {
            if (o1.position < o2.position) {
                transformed.position--;
            }
        } else {
            if (o1.position <= o2.position) {
                transformed.position--;
            }
        }
        return transformed;
    }
}