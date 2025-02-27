package com.project.contact.api.service;

import com.project.contact.api.dto.ContactDto;
import com.project.contact.api.dto.ContactResponse;
import com.project.contact.api.dto.ContactResponseWrapper;
import com.project.contact.api.model.Contact;
import com.project.contact.api.repository.ContactRepository;
import com.project.contact.api.util.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

import static com.project.contact.api.util.Constants.PHOTO_DIRECTORY;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@Service
@Slf4j
@Transactional( rollbackOn = Exception.class)
@RequiredArgsConstructor
public class ContactService {
    private final ContactRepository contactRepository;

    public ContactResponseWrapper createContact(ContactDto contactDto) {

        ContactResponseWrapper contactResponseWrapper = new ContactResponseWrapper();
        try{
            Contact contact = Contact.builder()
                    .name(contactDto.getName())
                    .email(contactDto.getEmail())
                    .title(contactDto.getTitle())
                    .phone(contactDto.getPhone())
                    .address(contactDto.getAddress())
                    .status(contactDto.getStatus())
                    .build();
            log.info("Contact :"+contact.toString());
            contactRepository.save(contact);
            log.info("Saving contact for ID : {}", contact.getId());
            List<Contact> contacts = new ArrayList<>();
            contacts.add(contact);
            List<ContactResponse> contactResponseList = buildContactResponseList(contacts);
            contactResponseWrapper.setContacts(contactResponseList);
            return contactResponseWrapper;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    public ContactResponseWrapper getContact(String id) {
        ContactResponseWrapper contactResponseWrapper = new ContactResponseWrapper();
        try{
            Optional<Contact> contactOptional = contactRepository.findById(id);
            if(contactOptional.isPresent()){
                Contact contact = contactOptional.get();
                List<Contact> contacts = new ArrayList<>();
                contacts.add(contact);
                List<ContactResponse> contactResponseList = buildContactResponseList(contacts);
                contactResponseWrapper.setContacts(contactResponseList);
            }
            else throw new ResourceNotFoundException("Contact is not present with id "+id);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

        return contactResponseWrapper;

    }

    public ContactResponseWrapper getAllContacts() {
        ContactResponseWrapper contactResponseWrapper = new ContactResponseWrapper();
        try{
            List<Contact> contacts = contactRepository.findAll();
            List<ContactResponse> contactResponseList = buildContactResponseList(contacts);
            contactResponseWrapper.setContacts(contactResponseList);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

        return contactResponseWrapper;
    }

    public ContactResponseWrapper updateContact(String id, ContactDto contactDto){

        ContactResponseWrapper contactResponseWrapper = new ContactResponseWrapper();
        try{
            Optional<Contact> contactOptional = contactRepository.findById(id);
            if(contactOptional.isPresent()){
                Contact contact = contactOptional.get();
                contact.setName(contactDto.getName());
                contact.setEmail(contactDto.getEmail());
                contact.setTitle(contactDto.getTitle());
                contact.setPhone(contactDto.getPhone());
                contact.setAddress(contactDto.getAddress());
                contact.setStatus(contactDto.getStatus());
                contactRepository.save(contact);
                List<Contact> contacts = new ArrayList<>();
                contacts.add(contact);
                List<ContactResponse> contactResponseList = buildContactResponseList(contacts);
                contactResponseWrapper.setContacts(contactResponseList);
                log.info("Contact is updated successfully with id : {}",id);
            }
            else throw new ResourceNotFoundException("Contact is not present with id "+id);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }


        return contactResponseWrapper;


    }

    public String deleteContact(String id) {
        Contact contact = contactRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Contact not found with id: " + id));
        contactRepository.deleteById(id);
        log.info("Contact is deleted successfully with id : {}",id);
        return "delete success";
    }

    public String uploadPhoto(String id, MultipartFile file) {
        ContactResponseWrapper contactResponseWrapper = getContact(id);
        ContactResponse contactResponse = contactResponseWrapper.getContacts().get(0);
        Contact contact = Contact.builder()
                .id(contactResponse.getId())
                .name(contactResponse.getName())
                .email(contactResponse.getEmail())
                .title(contactResponse.getTitle())
                .phone(contactResponse.getPhone())
                .address(contactResponse.getAddress())
                .status(contactResponse.getStatus())
                .build();
        String photoUrl = photoFunction.apply(id, file);
        contact.setPhotoUrl(photoUrl);
        contactRepository.save(contact);
        return photoUrl;
    }

    private final Function<String, String> fileExtension = fileName -> Optional.of(fileName).filter(name -> name.contains("."))
            .map(name -> "."+ name.substring(fileName.lastIndexOf(".")+1)).orElse(".png");

    private final BiFunction<String, MultipartFile, String> photoFunction = (id, image) -> {
        String fileName = id+fileExtension.apply(image.getOriginalFilename());
        try {
            Path fileStorageLocation = Paths.get(PHOTO_DIRECTORY).toAbsolutePath().normalize();
            if(!Files.exists(fileStorageLocation)){Files.createDirectories(fileStorageLocation);}
            Files.copy(image.getInputStream(),fileStorageLocation.resolve(fileName), REPLACE_EXISTING );
            return ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/contacts/image/"+fileName)
                    .toUriString();
        } catch (Exception e) {
            throw new RuntimeException("Unable to save image");
        }
    };

    private List<ContactResponse> buildContactResponseList(List<Contact> contacts) {
        List<ContactResponse> contactResponseList = new ArrayList<>();
        contacts.stream().forEach(contact -> {
            ContactResponse c = buildContactResponse(contact);
            contactResponseList.add(c);
        });
        return contactResponseList;
    }

    private ContactResponse buildContactResponse(Contact contact) {
        ContactResponse contactResponse = new ContactResponse();
        contactResponse.setId(contact.getId());
        contactResponse.setName(contact.getName());
        contactResponse.setEmail(contact.getEmail());
        contactResponse.setTitle(contact.getTitle());
        contactResponse.setPhone(contact.getPhone());
        contactResponse.setAddress(contact.getAddress());
        contactResponse.setPhotoUrl(contact.getPhotoUrl());
        contactResponse.setStatus(contact.getStatus());
        return contactResponse;
    }
}

