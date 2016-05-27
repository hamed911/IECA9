package net.internetengineering.domain.dealing;


import net.internetengineering.exception.DataIllegalException;

/**
 * Created by Hamed Ara on 2/18/2016.
 */
public class SellingOffer extends Offering{
    private String sellerID;

    public SellingOffer(Long dbID,Long price, Long quantity, String type, String sellerID) {
        super(dbID,price, quantity, type);
        this.sellerID = sellerID;
    }

    @Override
    protected void validate() throws DataIllegalException {
        if(sellerID==null || sellerID.isEmpty())
            throw new DataIllegalException("Mismatched Parameters");
    }

    @Override
    public String getID() {
        return sellerID;
    }
}
