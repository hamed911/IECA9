package net.internetengineering.domain.dealing;


import net.internetengineering.exception.DataIllegalException;

/**
 * Created by Hamed Ara on 2/18/2016.
 */
public abstract class Offering {
    private Long dbID;
    private Long price;
    private Long quantity;
    private String type;

    public String getType() {
        return type;
    }

    public Offering(Long dbID, Long price, Long quantity, String type) {
        this.dbID = dbID;
        this.price = price;
        this.quantity = quantity;
        this.type = type;
    }

    public void validateVariables() throws DataIllegalException {
        if(price==null ||price<0 || quantity==null ||quantity<0 || type==null || type.isEmpty())
            throw new DataIllegalException("Mismatched Parameters");
        validate();
    }

    protected abstract void validate()throws DataIllegalException;

    public Boolean typeIsMatched(String type){
        return this.type.equals(type);
    }

    public abstract String getID();

    public Boolean isAdminOffer(){
        return getID().equals("1");
    }

    public Long getPrice() {
        return price;
    }
    
    public void setPrice(Long p) {
		this.price = p;
	}
    
    public Long getQuantity(){
    	return quantity;
    }
    
    public void setQuantity(String type,Long count){
    	if(type.equals("add"))
    		this.quantity += count;
    	else if(type.equals("delete") && HasQuantity(count))
    		this.quantity -= count;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Boolean HasQuantity(Long count){
    	if(count <= this.quantity)
    		return true;
    	return false;
    }

    public void setDbID(Long dbID) {
        this.dbID = dbID;
    }

    public Long getDbID() {
        return dbID;
    }
    
    
}
