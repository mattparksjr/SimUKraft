package dev.simukraft.data.pack;

public class Structure {

    private String id;
    private String name;
    private String category;
    private String file;
    private String author;

    public Structure(String id, String name, String category, String file, String author) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.file = file;
        this.author = author;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
