package ru.plastikam.mail.ui;

import ru.plastikam.mail.model.AbstractEntity;
import ru.plastikam.mail.repository.AbstractRepository;

import java.io.Serializable;
import java.util.List;

public abstract class InMemoryTableBacking<T extends AbstractEntity> implements Serializable {

    public InMemoryTableBacking() {
    }

    protected abstract AbstractRepository<T> getRepository();

    protected List<T> load() {
        return getRepository().findAll();
    }

    private List<T> objects;

    private List<T> objectsFiltered;

    public List<T> getObjects() {
        if (objects == null) {
            objects = load();
        }
        return objects;
    }

    public List<T> getObjectsFiltered() {
        return objectsFiltered;
    }

    public void setObjectsFiltered(List<T> objectsFiltered) {
        this.objectsFiltered = objectsFiltered;
    }


}
