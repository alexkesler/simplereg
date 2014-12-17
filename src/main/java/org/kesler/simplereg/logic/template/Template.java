package org.kesler.simplereg.logic.template;

import org.kesler.simplereg.dao.AbstractEntity;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Created by alex on 17.12.14.
 */
@Entity
public class Template extends AbstractEntity{
    @Column(name = "Name", length = 512)
    private String name;
    @Column(name = "Data",length = 100000)
    private byte[] data;
    @Column(name = "ByDefault")
    private Boolean byDefault;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public Boolean getByDefault() {
        return byDefault;
    }

    public void setByDefault(Boolean byDefault) {
        this.byDefault = byDefault;
    }
}
