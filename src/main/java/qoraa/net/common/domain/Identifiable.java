package qoraa.net.common.domain;

import java.io.Serializable;

public interface Identifiable<ID extends Serializable> {

    /**
     * Returns the id of the entity.
     *
     * @return the id
     */
    ID getId();
}
