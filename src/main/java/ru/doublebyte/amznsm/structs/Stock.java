package ru.doublebyte.amznsm.structs;

/**
 * Stock item
 */
public class Stock {

    private String id;
    private String link;
    private String name;
    private String price;
    private String stock;

    ///////////////////////////////////////////////////////////////////////////

    public Stock() {

    }

    public Stock(String id, String link) {
        this.id = id;
        this.link = link;
    }

    @Override
    public String toString() {
        return String.format("Stock{id='%s', link='%s', name='%s', price='%s', stock='%s'}", id, link, name, price, stock);
    }

    ///////////////////////////////////////////////////////////////////////////

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

}
