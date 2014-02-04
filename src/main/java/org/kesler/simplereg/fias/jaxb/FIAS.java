package org.kesler.simplereg.fias.jaxb;

import org.kesler.simplereg.fias.FIASRecord;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * Created by Алексей on 03.02.14.
 */

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement
public class FIAS {

    @XmlElementWrapper(name="Objects")
    @XmlElement(name = "Object")
    private List<FIASRecord> records;

    public List<FIASRecord> getRecords() {return records;}

    public void setRecords(List<FIASRecord> records) {this.records = records;}
}
