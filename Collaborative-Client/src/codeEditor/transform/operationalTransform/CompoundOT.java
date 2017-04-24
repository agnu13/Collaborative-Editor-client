package codeEditor.transform.operationalTransform;

import codeEditor.operation.Operation;
import java.util.ArrayList;

public class CompoundOT {
    public static ArrayList<Operation> performTransform(String userId, ArrayList<Operation> localOperations, ArrayList<Operation> remoteOperations) {    
            ArrayList<Operation> transformedOperations = new ArrayList<>();
            for (Operation remote: remoteOperations) {
                if (remote.userId.equals(userId)) {
                    for (int i = 0; i < localOperations.size(); i++) {
                        if (localOperations.get(i).operationId.equals(remote.operationId)) {
                            localOperations.remove(i);
                            break;
                        }
                    }
                } else {
                    ArrayList<Operation> newLocalOperations = new ArrayList<>();
                    for (Operation local: localOperations) {
                        Operation transformed1 = OperationalTransform.transform(local, remote, false);
                        Operation transformed2 = OperationalTransform.transform(remote, local, true);
                        remote = transformed1;
                        newLocalOperations.add(transformed2);
                    }
                    localOperations.clear();
                    localOperations.addAll(newLocalOperations);
                    transformedOperations.add(remote);
                }
            }
            return transformedOperations;
    }
}