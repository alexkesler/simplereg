package org.kesler.simplereg.pvdimport.domain;


public class Applicant {
    private String id;
    private String idSubject;
    private Subject subject;
    private String idAgent;
    private Subject agent;

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public String getIdSubject() { return idSubject; }

    public void setIdSubject(String idSubject) { this.idSubject = idSubject; }

    public Subject getSubject() { return subject; }

    public void setSubject(Subject subject) { this.subject = subject; }

    public String getIdAgent() { return idAgent; }

    public void setIdAgent(String idAgent) { this.idAgent = idAgent; }

    public Subject getAgent() { return agent; }

    public void setAgent(Subject agent) { this.agent = agent; }

    public String getName() {
        return (subject==null?"":subject.getName()) + (agent==null?"":"("+agent.getName()+")");
    }
}
