package net.internetengineering.domain.dealing.types;

import net.internetengineering.domain.dealing.BuyingOffer;
import net.internetengineering.domain.dealing.Instrument;
import net.internetengineering.domain.dealing.SellingOffer;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import net.internetengineering.exception.DBException;


public class MPO implements ITypeExecutor {

	@Override
	public void sellingExecute(PrintWriter out, SellingOffer offer, List<SellingOffer> sellingOffers, List<BuyingOffer> buyingOffers, String symbol, Connection dbConnection) throws DBException,SQLException{
		Long count = offer.getQuantity();
    	for (int i = 0; i < buyingOffers.size(); i++) {
			count -= buyingOffers.get(i).getQuantity();
		}
    	if(count > 0){
    		out.println("Order is declined");
    		return;
    	}
		offer.setPrice(0L);
		sellingOffers.add(offer);
    	Instrument.sortOfferingListByPrice(sellingOffers);
    	Instrument.matchingOffers(out,true,sellingOffers,buyingOffers,symbol,this.getClass().getName(),dbConnection);
	}

	@Override
	public void buyingExecute(PrintWriter out, BuyingOffer offer, List<SellingOffer> sellingOffers, List<BuyingOffer> buyingOffers,String symbol, Connection dbConnection) throws DBException,SQLException{
		Long count = offer.getQuantity();
    	for (int i = 0; i < sellingOffers.size(); i++) {
			count -= sellingOffers.get(i).getQuantity();
		}
    	if(count > 0){
    		out.println("Order is declined");
    		return;
    	}
		offer.setPrice(Long.MAX_VALUE);
    	buyingOffers.add(offer);
    	Instrument.sortOfferingListByPrice(buyingOffers);
    	Instrument.matchingOffers(out,false,sellingOffers,buyingOffers,symbol,this.getClass().getName(),dbConnection);
	}

}
