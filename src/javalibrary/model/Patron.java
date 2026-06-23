package javalibrary.model;

// Representa um usuário cadastrado na biblioteca.
public class Patron {

    private String id;
    private String name;
    private String contact;

    public Patron(String id, String name, String contact) {
        setId(id);
        setName(name);
        setContact(contact);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("O ID do usuário não pode ficar vazio.");
        }

        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("O nome do usuário não pode ficar vazio.");
        }

        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        if (contact == null || contact.isBlank()) {
            throw new IllegalArgumentException("O contato do usuário não pode ficar vazio.");
        }

        this.contact = contact;
    }

    @Override
    public String toString() {
        return name + " (ID: " + id + ")";
    }
}
