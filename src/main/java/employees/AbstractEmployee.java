package employees;

public abstract class AbstractEmployee {
    private final int id;
    private final String name;

    public AbstractEmployee(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() { return id; }
    public String getName() { return name; }

    @Override
    public String toString() {
        return "Employee{" + "id=" + id + ", name='" + name + '\'' + '}';
    }
}

