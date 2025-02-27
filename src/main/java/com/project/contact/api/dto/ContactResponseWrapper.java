package com.project.contact.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class ContactResponseWrapper {
    @JsonProperty("contacts")
    private List<ContactResponse> contacts = new ArrayList<>();
    public void setContacts(List<ContactResponse> contactsRes) { this.contacts = contactsRes; }
    public List<ContactResponse> getContacts() { return this.contacts; }
}
