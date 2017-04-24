package projectManager;

public abstract class CollectionFactory {
    public static Collections getCollection(String userId) {
        Collections collection = new Collections(userId , "");
        return collection;
    }
}
